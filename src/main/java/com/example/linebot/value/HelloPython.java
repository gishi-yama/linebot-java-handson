package com.example.linebot.value;

import com.fasterxml.jackson.annotation.JsonCreator;

public class HelloPython {

  String message;

  @JsonCreator
  public HelloPython(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
