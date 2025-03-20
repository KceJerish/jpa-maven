package com.example.jpamaven.person;

import jakarta.persistence.Entity;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
public class FullTimePerson extends Person {

    private BigDecimal salary;

    public FullTimePerson(String name, BigDecimal salary) {
        super(name);
        this.salary = salary;
    }
}
