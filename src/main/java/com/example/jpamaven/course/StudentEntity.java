package com.example.jpamaven.course;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Entity
@Data
@Table(name = "student")
public class StudentEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Embedded
    private StudentLocation address;

    @OneToOne
    @JoinColumn(name = "custom_column")
    private Passport passport;

    @ManyToMany(fetch = FetchType.EAGER)
    // new table and  table columns
    @JoinTable(
            name = "Student_course_map_table",
            joinColumns = @JoinColumn(name = "student"),
            inverseJoinColumns = @JoinColumn(name = "course"))
    private List<Course> courses = new ArrayList<>();

    public StudentEntity(String name) {
        this.name = name;
    }

    public StudentEntity() {}

    public void addPassport(Passport passport) {
        passport.setStudent(this);
        this.passport = passport;
    }

    public void addCourse(Course course) {
        courses.add(course);
    }
}
