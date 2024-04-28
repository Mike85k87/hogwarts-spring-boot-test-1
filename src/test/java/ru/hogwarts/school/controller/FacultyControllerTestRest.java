package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FacultyControllerTestRest {
    @LocalServerPort
    private int port;
    @Autowired
    FacultyRepository facultyRepository;
    @Autowired
    private FacultyController facultyController;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoadsFaculty() throws Exception {
        Assertions.assertThat(facultyController).isNotNull();

    }

    @Test
    void getFacultyInfo() throws Exception {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/faculty/1", String.class))
                .isNotNull();
    }

    @Test
    void createFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("facultyTest");
        faculty.setColor("facultyColorTest");
        Assertions
                .assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/faculty", faculty, String.class))
                .isNotNull();
    }

    @Test
    void editFaculty() throws Exception {
        Faculty faculty1 = new Faculty(5L, "facultyTest", "facultyColorTest", null);
        facultyRepository.save(faculty1);
        Faculty faculty2 = new Faculty(5L,"TEST2","brown",null);

        ResponseEntity<Faculty> response = restTemplate.exchange("http://localhost:" + port + "/faculty",
                HttpMethod.PUT,
                new HttpEntity<>(faculty2),
                Faculty.class);

        Assertions
                .assertThat(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    void deleteFaculty() throws Exception {
        Long id = 102L;
        ResponseEntity<Void> responseEntity = restTemplate.exchange("/faculty/{id}",
                HttpMethod.DELETE, null, Void.class, id);
        Assertions.
                assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getFacultyByColor() throws Exception {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/faculty/facultyByColor?color=red", String.class))
                .isNotNull();
    }

    @Test
    void findFacultyByNameOrColor() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(2L);
        faculty.setName("IT");
        faculty.setColor("red");
        facultyController.createFaculty(faculty);

        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/faculty/facultyByNameOrColor?name=" + faculty.getName(), String.class))
                .isNotNull();
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/faculty/facultyByNameOrColor?color=" + faculty.getColor(), String.class))
                .isNotNull();
    }

    @Test
    void getStudentsOfFaculty() throws Exception {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/faculty/studentsOfFaculty?facultyId=1", String.class))
                .isNotNull();
    }
}