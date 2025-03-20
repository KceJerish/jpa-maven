package com.example.jpamaven.person;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;

@AllArgsConstructor
// @Component
public class HeirarchyRunner implements CommandLineRunner {

    //    private final PersonRepository personRepository;
    private final PartTimeRepo partTimeRepo;
    private final FullTimeRepo fullTimeRepo;

    @Override
    public void run(String... args) throws Exception {

        // @inheritance
        //        personRepository.save(new PartTimePerson("jerry", BigDecimal.valueOf(20)));
        //        personRepository.save(new FullTimePerson("jerry", BigDecimal.valueOf(20)));
        //       var p =  personRepository.findAll();
        //        System.out.println(p);

        // @mappedSUperclass
        //        partTimeRepo.save(new PartTimePerson("jerry", BigDecimal.valueOf(20)));
        //        fullTimeRepo.save(new FullTimePerson("jerry", BigDecimal.valueOf(20)));

    }
}
