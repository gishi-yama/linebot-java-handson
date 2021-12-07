package com.example.linebot.repository;

import com.example.linebot.value.HelloPython;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

// Databaseと同じように、Pythonとやりとりを行うRepository
@Repository
public class PythonRepository {

  private RestTemplate restTemplate;

  @Autowired
  public PythonRepository(RestTemplateBuilder templateBuilder) {
    this.restTemplate = templateBuilder.build();
  }

  public HelloPython findPythonAPI() {
    // Pythonの処理結果（JSON）をHelloPythonのインスタンスにマッピングする
    HelloPython response =
      restTemplate.getForObject("http://localhost:8080/hello", HelloPython.class);
    return response;
  }

}
