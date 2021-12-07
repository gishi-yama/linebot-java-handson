package com.example.linebot.replier;

import com.example.linebot.value.HelloPython;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

public class PythonGreet implements Replier {

  private HelloPython helloPython;

  public PythonGreet(HelloPython helloPython) {
    this.helloPython = helloPython;
  }

  @Override
  public Message reply() {
    String body = String.format("Pythonプログラムから: %s", helloPython.getMessage());
    return new TextMessage(body);
  }
}
