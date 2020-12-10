package com.example.linebot.replier;

import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

import java.time.LocalTime;

// 挨拶用の返信クラス
public class Greet implements Replier {

  @Override
  public Message reply() {
    LocalTime lt = LocalTime.now();
    int hour = lt.getHour();
    if (hour >= 17) {
      return new TextMessage("こんばんは！");
    }
    if (hour >= 11) {
      return new TextMessage("こんにちは！");
    }
    return new TextMessage("おはよう！");
  }

}
