package com.example.jpamaven.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TransactionalTesting {

    @Autowired
    private PassportRepository passportRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Transactional
    protected void doSomethingTx() {
        var p = passportRepository.save(new Passport("8967"));
        var e = new StudentEntity("what");
        e.addPassport(p);
        studentRepository.saveAndFlush(e);
        e.setName("the hell");
        throw new RuntimeException("test error");
        //        System.out.println(studentRepository.findById(e.getId()));
    }
}
