package com.example.jpamaven;

import com.example.jpamaven.course.Course;
import com.example.jpamaven.course.StudentEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.logging.Logger;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = JpaMavenApplication.class)
public class CriteriaQueryTest {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @PersistenceContext
    private EntityManager em;

    @Test
    public void testCriteriaQuery() {
        TypedQuery query = em.createQuery("SELECT e FROM Course e", Course.class);
        query.getResultList().forEach(System.out::println);

        CriteriaBuilder cb = em.getCriteriaBuilder();
        var cq = cb.createQuery(Course.class);
        Root<Course> root = cq.from(Course.class);
        TypedQuery<Course> query1 = em.createQuery(cq.select(root));
        query1.getResultList().forEach(x -> System.out.println(x.getName()));
    }

    @Test
    public void testCriteriaQueryNotLike() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        var cq = cb.createQuery(Course.class);

        Root<Course> root = cq.from(Course.class);
        Predicate p = cb.isEmpty(root.get("students"));
        cq.where(p);

        TypedQuery<Course> query1 = em.createQuery(cq.select(root));
        query1.getResultList().forEach(x -> System.out.println(x.getName()));
    }

    @Test
    public void testCriteriaQueryJoin() {
        // "select * from course c join students"
        CriteriaBuilder cb = em.getCriteriaBuilder();
        var cq = cb.createQuery(Course.class);

        Root<Course> root = cq.from(Course.class);
        Join<Object, Object> j = root.join("students");
        //        Predicate p = cb.isEmpty(root.get("students"));
        //        cq.where(p);

        TypedQuery<Course> query1 = em.createQuery(cq.select(root));
        query1.getResultList().forEach(x -> System.out.println(x.getName()));
    }

    @Test
    public void testCriteriaQueryLeftJoin() {
        // "select * from course c left join students"
        CriteriaBuilder cb = em.getCriteriaBuilder();
        var cq = cb.createQuery(Course.class);

        Root<Course> root = cq.from(Course.class);
        Join<Object, Object> j = root.join("students", JoinType.LEFT);
        //        Predicate p = cb.isEmpty(root.get("students"));
        //        cq.where(p);

        TypedQuery<Course> query1 = em.createQuery(cq.select(root));
        query1.getResultList().forEach(x -> System.out.println(x.getName()));
    }

    @Test
    public void testCriteriaQueryLeftJoinStudents() {
        // "select * from course c left join students"
        CriteriaBuilder cb = em.getCriteriaBuilder();
        var cq = cb.createQuery(StudentEntity.class);

        Root<StudentEntity> root = cq.from(StudentEntity.class);
        root.join("courses", JoinType.LEFT);
        //        Predicate p = cb.isEmpty(root.get("students"));
        //        cq.where(p);

        TypedQuery<StudentEntity> query1 = em.createQuery(cq.select(root));
        query1.getResultList().forEach(x -> System.out.println(x.getName()));
    }
}
