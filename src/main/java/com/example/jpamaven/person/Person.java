package com.example.jpamaven.person;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
// @Table(name = "person")
@NoArgsConstructor
@ToString
@MappedSuperclass
// @Entity
// @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
// @DiscriminatorColumn(name = "emp_type") //column name to say what sub type this is for SINGLE table
public abstract class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    public Person(String name) {
        this.name = name;
    }
}
