package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTestRest {
    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;
    @Autowired
    private FacultyController facultyController;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoadsStudent() throws Exception {
        Assertions.assertThat(studentController).isNotNull();
    }

    @Test
    void getStudentInfo() throws Exception {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student/2", String.class))
                .isNotNull();
    }

    @Test
    void createStudent() throws Exception {
        Student student = new Student();
        student.setName("Karl");
        student.setAge(40);
        Assertions
                .assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/student", student, String.class))
                .isNotNull();

    }

    @Test
    void editStudent() throws Exception {
        Student student1 = new Student(2L,"Karl",40,null);
        studentController.createStudent(student1);

        Student student2 = new Student(2L,"Hagrid",60, null);

        ResponseEntity<Student> response = restTemplate.exchange("http://localhost:" + port + "/student",
                HttpMethod.PUT,
                new HttpEntity<>(student2),
                Student.class);

        Assertions
                .assertThat(response.getBody().getName()).isEqualTo("Hagrid");
    }

    @Test
    void deleteStudent() throws Exception {
        Long id = 252L;
        ResponseEntity<Void> responseEntity = restTemplate.exchange("/student/{id}",
                HttpMethod.DELETE, null, Void.class, id);
        Assertions.
                assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void findAllByAgeBetween() throws Exception {
        Student student1 = new Student(2L, "maxim", 20,null);  //Создаем ожидаемых из базы студентов
        studentController.createStudent(student1);
        Student student2 = new Student(3L, "vasiliy", 25, null);
        studentController.createStudent(student2);
        Student student3 = new Student(4L, "alexey", 30, null);
        studentController.createStudent(student3);

        var result = restTemplate.getForObject("http://localhost:" + port + "/student/ageBetween?minAge=20&maxAge=30", String.class);
        assertThat(result).isNotNull();
    }

    @Test
    void getFacultyOfStudent() throws Exception {
        Faculty faculty1 = facultyController.createFaculty(new Faculty(20L, "Test", "fhgdbd", null));
        Student student1 = studentController.createStudent(new Student(100L, "Lucius", 40, faculty1));
        Faculty actual = this.restTemplate.getForObject("http://localhost:" + port + "/student/facultyOfStudent?studentId=" + student1.getId(), Faculty.class);
        assertThat(actual.getId()).isEqualTo(faculty1.getId());
    }

    @Test
    void uploadAvatar() throws Exception {
        byte[] avatar = new byte[1024];
        long id = 1;
        LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.put("avatar", Collections.singletonList(new ByteArrayResource(avatar)));
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                RequestEntity.post("/{id}/avatar", id)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .body(body), String.class);
        assertNotNull(responseEntity);
    }

    @Test
    void downloadAvatarPreview() throws Exception {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student/2/avatar/preview", String.class))
                .isNotNull();
    }

    @Test
    void DownloadAvatar() throws Exception {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student/facultyOfStudent?studentId=2", String.class))
                .isNotNull();
        }
}
