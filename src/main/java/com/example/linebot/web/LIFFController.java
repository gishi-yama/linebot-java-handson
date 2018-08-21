package com.example.linebot.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LIFFController {

  @GetMapping("/liff")
  public String hello(Model model) {
    model.addAttribute("test", "Hello Tymeleaf!");
    return "liff";
  }

}