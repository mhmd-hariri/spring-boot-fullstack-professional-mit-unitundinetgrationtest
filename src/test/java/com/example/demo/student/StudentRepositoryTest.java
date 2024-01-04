package com.example.demo.student;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldCheckIfStudentExistsEmail() {
        //given
        String email = "s.hareere500@gmail76.com";
        Student student = new Student(
                "Shareef",
                "s.hareere500@gmail76.com",
                Gender.MALE
        );
        underTest.save(student);
        //when
       boolean expected = underTest.selectExistsEmail(email);
        //then
        assertThat(expected).isTrue();
    }
}