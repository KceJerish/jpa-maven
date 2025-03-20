package com.example.jpamaven;

import java.util.Date;
import org.springframework.boot.CommandLineRunner;

// @Service
public class MyRunner implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;

    public MyRunner(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        var emp = new Employee("jerry", 32, new Date());
        emp.addAddress(new Address("my street"));
        employeeRepository.save(emp);
    }
}
