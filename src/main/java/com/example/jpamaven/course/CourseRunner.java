package com.example.jpamaven.course;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CourseRunner implements CommandLineRunner {

    private CourseRepository courseRepository;
    private TransactionalTesting transactionalTesting;
    private CourseRepositoryImpl courseRepositoryImpl;
    private PassportRepository passportRepository;
    private StudentRepository studentRepository;
    private ReviewRepository reviewRepository;

    @Override
    //        @Transactional
    public void run(String... args) throws Exception {

        passportRepository.save(new Passport("1234"));
        passportRepository.save(new Passport("5678"));
        var p = passportRepository.save(new Passport("8967"));
        passportRepository.save(new Passport("0987"));

        var e = new StudentEntity("jerry");
        e.addPassport(p);
        studentRepository.save(e);
        studentRepository.save(new StudentEntity("james"));
        studentRepository.save(new StudentEntity("jerish"));

        reviewRepository.save(new ReviewEntity(Rating.ONE, "ok"));
        reviewRepository.save(new ReviewEntity(Rating.FOUR, "ok"));
        reviewRepository.save(new ReviewEntity(Rating.FIVE, "ok"));

        var maths = new Course("Maths");
        var review = new ReviewEntity(Rating.TWO, "ok");
        var review2 = new ReviewEntity(Rating.THREE, "okies");
        maths.addReview(review);
        maths.addReview(review2);
        courseRepository.save(maths);
        var science = new Course("science");
        courseRepository.save(science);
        courseRepository.save(new Course("social"));
        System.out.println("size=" + courseRepository.findEmptyCourse().size());

        maths.clearReview();
        maths.addReview(new ReviewEntity(Rating.THREE, "okies"));
        courseRepository.save(maths);

        var student = new StudentEntity("who");
        student.setAddress(new StudentLocation("location", "city"));
        student.addCourse(maths);
        student.addCourse(science);
        studentRepository.save(student);

        var stud = studentRepository.findByName("who");

        studentRepository.findAll(Sort.by(Sort.Direction.DESC, "id").and(Sort.by(Sort.Direction.ASC, "name")));
        courseRepository.deleteById(3L);

        //        var p1 = passportRepository.findById(p.getId());
        //        System.out.println(p1.get());
        //        System.out.println(p1.get().getStudent());

        //        e0.setName("social");
        //        var e1 =new Course("Maths1");
        //        courseRepositoryImpl.getEntityManager().persist(e1);
        //        e1.setName("science");

        //        System.out.println(courseRepository.findAll().toString());
        //        System.out.println(courseRepository.findNameLike("Math").toString());

        //        System.out.println(studentRepository.findByName("james"));
        //        System.out.println(studentRepository.findByName("jerry"));
        //        try {
        //            transactionalTesting.doSomethingTx();
        //        }catch(Exception ex){
        //            ex.printStackTrace();
        //        }
        //        System.out.println(studentRepository.findByName("what"));
        //        System.out.println(studentRepository.findByName("the hell"));
    }
}
