package com.example.jpamaven.person;

import org.springframework.data.jpa.repository.JpaRepository;

// @Repository //this wont work for @mappedsuperclass
public interface PersonRepository extends JpaRepository<Person, Long> {}
