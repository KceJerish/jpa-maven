package com.example.jpamaven.person;

import org.springframework.data.jpa.repository.JpaRepository;

// @Repository
public interface FullTimeRepo extends JpaRepository<FullTimePerson, String> {}
