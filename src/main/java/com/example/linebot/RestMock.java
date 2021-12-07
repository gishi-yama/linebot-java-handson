package com.example.linebot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestMock {

  @GetMapping("hello")
  Hello get() {
    return new Hello("Hello_World");
  }

  class Hello {
    String message;

    public Hello(String message) {
      this.message = message;
    }

    public String getMessage() {
      return message;
    }
  }

}
