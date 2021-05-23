package com.example.linebot.service;

import com.example.linebot.replier.PythonGreet;
import com.example.linebot.repository.PythonRepository;
import com.example.linebot.value.HelloPython;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExternalService {

  private PythonRepository repository;

  @Autowired
  public ExternalService(PythonRepository repository) {
    this.repository = repository;
  }

  public PythonGreet doReplyWithPython() {
    HelloPython helloPython = repository.findPythonAPI();
    return new PythonGreet(helloPython);
  }

}
