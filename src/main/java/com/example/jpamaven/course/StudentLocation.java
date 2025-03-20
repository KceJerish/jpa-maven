package com.example.jpamaven.course;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class StudentLocation {

    private String location;
    private String city;

    public StudentLocation() {}

    public StudentLocation(String location, String city) {
        this.location = location;
        this.city = city;
    }
}
