package com.example.linebot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LinebotApplication {

  public static void main(String[] args) {
    SpringApplication.run(LinebotApplication.class, args);
  }

}
