package com.example.jpamaven.course;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Version;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter
@Setter
public class Course {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Version
    private int version;

    //    @SoftDelete
    private boolean active;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;

    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER)
    private List<ReviewEntity> reviews = new ArrayList<>();

    @ManyToMany(mappedBy = "courses")
    private List<StudentEntity> students = new ArrayList<>();

    public Course(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Course(String name) {
        this.name = name;
        this.active = true;
    }

    public Course() {}

    public void addReview(ReviewEntity review) {
        review.setCourse(this);
        reviews.add(review);
    }

    public void addStudent(StudentEntity student) {
        students.add(student);
    }

    public void clearReview() {
        reviews.clear();
    }
}
