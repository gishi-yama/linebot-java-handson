package com.example.linebot;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class Test {

  @RequestMapping(method = RequestMethod.GET)
  public String hello() {
    return "Hello!";
  }

}
