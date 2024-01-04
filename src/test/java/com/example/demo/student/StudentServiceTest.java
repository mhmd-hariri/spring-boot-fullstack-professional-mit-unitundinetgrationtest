package com.example.demo.student;

import com.example.demo.student.exception.BadRequestException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    private StudentService underTest;
    @Mock
    private StudentRepository studentRepository;

    @BeforeEach
    void setUp() {
        underTest = new StudentService(studentRepository);
    }

    @Test
    void canGetAllStudents() {
        //when
        underTest.getAllStudents();
        //then
        verify(studentRepository)
                .findAll();
    }

    @Test
    void canAddStudent() {
        // when
        Student student = new Student(
                "Shareef",
                "s.hareere500@gmail76.com",
                Gender.MALE
        );
        underTest.addStudent(student);
        //Then
        ArgumentCaptor<Student> studentArgumentCaptor  = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository)
                .save(studentArgumentCaptor.capture());
        Student capturedStudent = studentArgumentCaptor.getValue();
        assertThat(capturedStudent).isEqualTo(student);
    }
    @Test
    void WillThrowWhenEmailTaken() {
        // given
        Student student = new Student(
                "Shareef",
                "s.hareere500@gmail76.com",
                Gender.MALE
        );
        given(studentRepository.selectExistsEmail(student.getEmail())).willReturn(true);
        //when
        //Then
        assertThatThrownBy(() -> underTest.addStudent(student))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining(  "Email " + student.getEmail() + " taken");
        verify(studentRepository,never()).save(any());
    }

    @Test
    @Disabled
    void deleteStudent() {
    }
}