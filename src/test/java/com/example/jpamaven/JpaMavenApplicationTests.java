package com.example.jpamaven;

import com.example.jpamaven.course.CourseRepository;
import com.example.jpamaven.course.StudentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = JpaMavenApplication.class)
class JpaMavenApplicationTests {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void contextLoads() {
        Assertions.assertTrue(courseRepository.findById(1L).isPresent());
    }

    @Test
    void testDelete() {
        courseRepository.deleteById(1L);
        Assertions.assertFalse(courseRepository.findById(1L).isPresent());
    }

    @Test
    void testStudent() {
        System.out.println(studentRepository.findByName("jerry"));
    }

    @Test
    @Transactional
    void testTransaction() {
        var c = courseRepository.findById(1L);
        System.out.println(c.isPresent());
        var c2 = courseRepository.findById(1L);
        System.out.println(c2.isPresent());
    }
}
