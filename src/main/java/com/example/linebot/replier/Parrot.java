package com.example.linebot.replier;

import com.linecorp.bot.messaging.model.Message;
import com.linecorp.bot.messaging.model.TextMessage;

// おうむ返し用の返信クラス
public class Parrot implements Replier {

  private String text;

  public Parrot(String text) {
    this.text = text;
  }

  @Override
  public Message reply() {
    return new TextMessage(text);
  }
}
