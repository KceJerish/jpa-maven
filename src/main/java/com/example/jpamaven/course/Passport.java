package com.example.jpamaven.course;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class Passport {

    @Id
    @GeneratedValue
    private long id;

    private String number;

    @OneToOne(mappedBy = "passport")
    private StudentEntity student;

    public Passport() {}

    public Passport(String number) {
        this.number = number;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public StudentEntity getStudent() {
        return student;
    }

    public void setStudent(StudentEntity student) {
        this.student = student;
    }

    @Override
    public String toString() {
        return "Passport{" + "id="
                + id + ", number='"
                + number + '\'' +
                //                ", student=" + student +
                '}';
    }
}
