import yargs from "yargs";
import * as path from "path";
import { glob } from "glob";
import * as fs from "fs";
import { CoreV1Api, KubeConfig } from "@kubernetes/client-node";
import { V1PodSpec } from "@kubernetes/client-node/dist/gen/model/v1PodSpec";

const CLUSTER_CONTEXT = "arn:aws:eks:us-east-2:994010719649:cluster/load01"; 
// const CLUSTER_CONTEXT = "arn:aws:eks:us-east-1:994010719649:cluster/load"; 
const NAMESPACE = "default";
// const NAMESPACE = "commerce";
const CONFIGMAP_NAME = "commerce-performance";
// const OUT_INFLUXDB_URL = "http://influxdb:8086/myk6db";
const K6_INFLUXDB_PUSH_INTERVAL = "1s";
const K6_INFLUXDB_CONCURRENT_WRITES = "16";

const argv = yargs(process.argv.slice(2))
  .scriptName("k8s")
  .usage("Usage: $0 [option]")
  .option("test", {
    array: false,
    type: "string",
    description: "the file to test",
    alias: "t",
  })
  .option("env", {
    array: false,
    type: "string",
    choices: ["dev", "stg"],
    default: "stg",
    description:
      "the environment to test against, DNA_ENVIRONMENT in test spec",
    alias: "e",
  })
  .option("memory", {
    array: false,
    type: "number",
    description: "RAM memory which will be requested (in GB)",
  })
  .option("cpu", {
    array: false,
    type: "number",
    description: "RAM memory which will be requested (in GB)",
  })
  .option("vu", {
    array: false,
    type: "string",
    description: "Number of Virtual User",
  })
  .option("setupScript", {
    array: false,
    type: "string",
    description: "Script that need to be run before the load test running. ex: GameServerLogin, SetupSteamReconciliation, SetupPurchaseWithLimit",
  })
  .option("tearDownScript", {
    array: false,
    type: "string",
    description: "Script that need to be run after the load test running. For example for clean up purpose. ex: TeardownResetInventory",
  })
  .option("configFile", {
    array: false,
    type: "string",
    description: "Path file config to running load test. ex: test\config.json",
  })
  .option("optionsFile", {
    array: false,
    type: "string",
    description: "Path file config to running load test. ex: test\options.json",
  })
  .demandOption("optionsFile")
  .demandOption("configFile")
  .demandOption("test")
  .demandOption("vu").argv;

runTest(argv);

async function runTest(args: typeof argv): Promise<void> {
  const environment: string = args.env;
  const parsedFile = path.parse(args.test);
  const pathFileConfig: string = args.configFile;

  const pathFileOptions: string = args.optionsFile;

  const filename = `${parsedFile.name}.js`;
  const fileContent = readTestFile(`dist/**/${filename}`);
  const fileContentConfig = readTestFile(pathFileConfig);
  const fileContentOptions = readTestFile(pathFileOptions);


  const api = createK8sApi();
  console.log(`using cluster: ${api.basePath}`);

  await clearOldConfigMap(api);
  let content : { [key: string]: string} = {};
  content[filename] = fileContent;
  content['options.json'] = fileContentOptions;
  content['config.json'] = fileContentConfig;

  await createConfigMap(api, content);
  await createPod(api, filename, environment, argv.vu, args.memory, args.cpu, args.setupScript, args.tearDownScript);
}

export function readTestFile(filename: string): string {
  const matches = glob.sync(filename);
  if (matches.length === 0) {
    throw new Error(`No matching test files ${filename}`);
  }

  return fs.readFileSync(matches[0], "base64");
}

export function createK8sApi(): CoreV1Api {
  const kc = new KubeConfig();
  kc.loadFromDefault();

  if (kc.currentContext !== CLUSTER_CONTEXT) {
    throw new Error(`Please switch kubernetes context to ${CLUSTER_CONTEXT}`);
  }

  return kc.makeApiClient(CoreV1Api);
}

export async function clearOldConfigMap(k8sApi: CoreV1Api): Promise<void> {
  const result = await k8sApi.listNamespacedConfigMap(NAMESPACE);
  if (result.response.complete) {
    const configMapExists = result.body.items.find(
      (item) => item.metadata?.name === CONFIGMAP_NAME
    );
    if (configMapExists) {
      console.log("Config map already exists.");
      await deleteOldConfigMap(k8sApi);
    }
  }
}

async function deleteOldConfigMap(api: CoreV1Api): Promise<void> {
  console.log("Deleting old config map...");
  try {
    await api.deleteNamespacedConfigMap(CONFIGMAP_NAME, NAMESPACE);
    console.log("Deleted old config map.");
  } catch (e) {
    throw new Error(
      "Failed to delete old config map. " +
        JSON.stringify(e.response.body, null, "\t")
    );
  }
}

export async function createConfigMap(
  api: CoreV1Api,
  content: {[key: string]: string }
): Promise<void> {
  console.log("Creating new config...");

  try {
    await api.createNamespacedConfigMap(NAMESPACE, {
      apiVersion: "v1",
      binaryData: content,
      kind: "ConfigMap",
      metadata: { name: CONFIGMAP_NAME },
    });

    console.log("Config map created.");
  } catch (e) {
    throw new Error(
      "Failed to create config map. " +
        JSON.stringify(e.response.body, null, "\t")
    );
  }
}

export async function createPod(
  api: CoreV1Api,
  filename: string,
  environment: string,
  vu: string,
  memory?: number,
  cpu?: number,
  setupScript?: string,
  tearDownScript?: string
): Promise<void> {
  console.log("Creating Pod...");

  const testName = `commerce-${Date.now()}-${filename.replace(
    /\..*$/,
    ""
  )}-${'loadtest'}`
    .toLocaleLowerCase()
    .substring(0, 63);

  try {
    const mountPath = "/home/k6/test";
    const mountPathSeeding = "/data"
    const awsBucket = "k6-runner-temporary-bucket"
    const spec: V1PodSpec = {
      serviceAccountName: "k6-operator-runner",
      initContainers: [{
        name: "download-script",
        image: "public.ecr.aws/aws-cli/aws-cli",
        command: ["sh", "-c", `aws s3 cp s3://${awsBucket}/seeding/ /scripts/ --recursive --exclude "*" --include "*${vu}.json" &&
            aws s3 cp s3://${awsBucket}/seeding/ /data/ --recursive --exclude "*" --include "*${vu}.json" &&
            aws s3 cp s3://${awsBucket}/config/ /data/ --recursive --exclude "*" --include "*.json" &&
            echo "ls /data/..." &&
            ls /data/ &&
            echo "start..."`],
        env: [
          {
            name: "AWS_DEFAULT_REGION",
            value: "us-west-2"
          },
        ],
        volumeMounts: [
          { name: "load-test", mountPath: mountPathSeeding},
          { name: "test", mountPath: mountPath }
        ],
      }],
      containers: [
        {
          name: "k6",
          image: "grafana/k6:0.44.0",
          env: [
            {
              name: "K6_INFLUXDB_TAGS_AS_FIELDS",
              value: "error,url",
            },
            {
              name: "K6_INFLUXDB_PUSH_INTERVAL",
              value: K6_INFLUXDB_PUSH_INTERVAL,
            },
            {
              name: "K6_INFLUXDB_CONCURRENT_WRITES",
              value: K6_INFLUXDB_CONCURRENT_WRITES,
            },
            {
              name: "DNA_ENVIRONMENT",
              value: environment,
            },
            {
              name: "USER_COUNT",
              value: vu,
            },
            {
              name: "SETUP_SCRIPT",
              value: setupScript,
            },
            {
              name: "TEARDOWN_SCRIPT",
              value: tearDownScript,
            },
          ],
          envFrom: [{
            secretRef: {
              name: 'datadog-secrets'
            } 
          }
          ],
          args: [
            "run",
            "--tag",
            `test_space=commerce`,
            "--tag",
            `test_run=${testName}`,
            `--compatibility-mode=base`,
            // "--out",
            // `influxdb=${OUT_INFLUXDB_URL}`,
            `${mountPath}/${filename}`,
          ],
          volumeMounts: [
            { name: "test", mountPath },
            { name: "load-test", mountPath: mountPathSeeding}
          ],
        },
      ],
      restartPolicy: "Never",
      automountServiceAccountToken: false,
      volumes: [
        {name: "load-test", emptyDir: {}},
        { name: "test", configMap: { name: CONFIGMAP_NAME }}
      ],
      nodeSelector: { "2kcoretech.online/node-role": "attacker" },
    };

    if (memory) {
      spec.containers[0].resources = {
        requests: {
          memory: `${memory}Gi`,
        },
      };
    }

    if (cpu) {
      spec.containers[0].resources = {
        requests: {
          cpu: `${cpu}Gi`,
        },
      };
    }

    await api.createNamespacedPod(NAMESPACE, {
      apiVersion: "v1",
      kind: "Pod",
      metadata: { name: testName, annotations: { "janitor/ttl": "24h" } },
      spec,
    });
    console.log(`Pod created: ${testName}`);
  } catch (e) {
    throw new Error(
      "Failed to create pod. " + JSON.stringify(e.response.body, null, "\t")
    );
  }
}
