package com.example.jpamaven.person;

import org.springframework.data.jpa.repository.JpaRepository;

// @Repository
public interface PartTimeRepo extends JpaRepository<PartTimePerson, String> {}
