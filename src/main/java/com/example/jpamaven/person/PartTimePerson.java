package com.example.jpamaven.person;

import jakarta.persistence.Entity;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
public class PartTimePerson extends Person {

    private BigDecimal wage;

    public PartTimePerson(String name, BigDecimal wage) {
        super(name);
        this.wage = wage;
    }
}
