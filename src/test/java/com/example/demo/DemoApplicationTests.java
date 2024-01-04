package com.example.demo;

import com.example.demo.student.Gender;
import com.example.demo.student.Student;
import com.example.demo.student.StudentH2Repository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class DemoApplicationTests {
	@LocalServerPort
	private int port;
	@Autowired
	private  StudentH2Repository studentH2Repository;
	private static RestTemplate restTemplate;
	private  String baseUrl = "http://localhost";

	@Autowired
	private MockMvc mockMvc;

	@BeforeAll
	public static  void init(){
		restTemplate = new RestTemplate();
	}
	@BeforeEach
	public void setBaseUrl(){
		baseUrl = baseUrl.concat(":").concat(port+"").concat("/api/v1/students");
		studentH2Repository.deleteAll();
	}

	@Test
	public void canAddStudent(){
				Student student = new Student(
				"Shareef",
				"s.hareere500@gmail76.com",
				Gender.MALE
		);
				restTemplate.postForObject(baseUrl,student,void.class);
				assertThat(1).isEqualTo(studentH2Repository.findAll().size());
	}
	@Test
	public void CanGetStudents() {
		Student student = new Student(
				"Shareef",
				"s.hareere500@gmail76.com",
				Gender.MALE
		);
		studentH2Repository.save(student);
	     List<Student> students =	restTemplate.getForObject(baseUrl, List.class);
	     assertEquals(1,students.size());
	     assertEquals(1,studentH2Repository.findAll().size());
	}
	@Test
	public void CanDeleteStudent(){
		Student student = new Student(
				"Shareef",
				"s.hareere500@gmail76.com",
				Gender.MALE
		);
		long studentId = studentH2Repository.save(student).getId();
		int recordCount = studentH2Repository.findAll().size();
		assertEquals(1,recordCount);
		restTemplate.delete(baseUrl+"/{studentId}",studentId);
		assertEquals(0,studentH2Repository.findAll().size());
	}
	Calculator underTest = new Calculator();

	@Test
	void contextLoads() {
	}

	@Test
	void itShouldAddTwoNumbers() {
		//given
		int num1 = 20;
		int num2 = 30;
		//when
		int result = underTest.add(num1,num2);
		//then
		int expected = 50;
		assertThat(result).isEqualTo(expected);
	}
	@Test
	void CanAddStudentWithMVCMock() throws Exception {
		Student student = new Student(
				"Shareef",
				"s.hareere500@gmail76.com",
				Gender.MALE
		);
		ObjectMapper om = new ObjectMapper();
		String jsonRequest = om.writeValueAsString(student);
		this.mockMvc
				.perform(post(baseUrl).content(jsonRequest).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andDo(print())
				.andReturn();
		assertThat(1).isEqualTo(studentH2Repository.findAll().size());
		assertThat(student.getName()).isEqualTo(studentH2Repository.findAll().get(0).getName());
	}

	class Calculator{
		int add(int a , int b) {
			return  a + b;
		}
	}

}
