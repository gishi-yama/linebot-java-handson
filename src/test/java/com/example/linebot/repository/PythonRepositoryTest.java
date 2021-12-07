package com.example.linebot.repository;

import com.example.linebot.value.HelloPython;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class PythonRepositoryTest {

  PythonRepository repository;

  @Autowired
  public PythonRepositoryTest(PythonRepository repository) {
    this.repository = repository;
  }

  @Test
  void Hello_WorldをRESTTemplateベースで取得できる() {
    HelloPython helloPython = repository.findPythonAPI();
    String actual = helloPython.getMessage();
    assertEquals("Hello_World", actual);
//    fail();
  }

}
