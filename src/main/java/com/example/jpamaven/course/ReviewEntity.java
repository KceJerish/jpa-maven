package com.example.jpamaven.course;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class ReviewEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private Rating rating;

    private String comment;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    public ReviewEntity(Rating rating, String comment) {
        this.rating = rating;
        this.comment = comment;
    }
}
