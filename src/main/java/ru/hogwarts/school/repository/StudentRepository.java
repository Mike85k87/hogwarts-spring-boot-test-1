package ru.hogwarts.school.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    // Collection<Student> findByAge(int age);

    List<Student> findAllByAgeBetween(Integer minAge, Integer maxAge);
    @Query (value = "select count(*) from student s", nativeQuery = true)
    Integer amountOfStudents ();
    @Query (value = "select avg(age) from student s ", nativeQuery = true)
    Integer averageAge();
    @Query (value = "select *from student s order by id desc limit 5",nativeQuery = true)
    List <Student> getLastStudents();
}
