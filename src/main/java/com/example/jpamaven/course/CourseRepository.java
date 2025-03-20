package com.example.jpamaven.course;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("select new Course(c.id, c.name) from Course c where c.name like  :name%")
    List<Course> findNameLike(@Param("name") String name);

    @Query("select c from Course c where c.students is empty ")
    List<Course> findEmptyCourse();
}
