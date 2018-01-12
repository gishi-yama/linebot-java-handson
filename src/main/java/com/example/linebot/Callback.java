package com.example.linebot;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalTime;

@LineMessageHandler
public class Callback {

  @EventMapping
  public TextMessage handleText(MessageEvent<TextMessageContent> event) {
    System.out.println("event: " + event);
    TextMessageContent tmc = event.getMessage();
    String text = tmc.getText();
    switch (text) {
      case "こんにちは":
        return makeGreeting();
      case "教室":
        return makeRoomInfo();
      default:
        return parrot(text);
    }
  }

  @EventMapping
  public void handleEvent(Event event) {
    System.out.println("event: " + event);
  }

  // オウム返しをする
  private TextMessage parrot(String text) {
    return new TextMessage(text);
  }

  // あいさつする
  private TextMessage makeGreeting() {
    LocalTime lt = LocalTime.now();
    int hour = lt.getHour();
    if (hour >= 6 && hour <= 11) {
      return new TextMessage("おはようございます、Dukeです");
    }
    if (hour >= 12 && hour <= 16) {
      return new TextMessage("こんにちは、Dukeです");
    }

    return new TextMessage("こんばんは、Dukeです");
  }

  // センサーの値をWebから取得して、CO2クラスのインスタンスにいれる(xxxxの所は、別途指示します）
  private TextMessage makeRoomInfo() {
    String key = "xxxx";
    String url = "https://us.wio.seeed.io/v1/node/GroveCo2MhZ16UART0/concentration_and_temperature?access_token=";
    URI uri = URI.create(url + key);
    RestTemplate restTemplate = new RestTemplateBuilder().build();
    try {
      CO2 co2 = restTemplate.getForObject(uri, CO2.class);
      String message = "二酸化炭素は" + co2.getConcentration() + "ppm、温度は" + co2.getTemperature() + "度です";
      return new TextMessage(message);
    } catch (HttpClientErrorException e) {
      e.printStackTrace();
      return new TextMessage("センサーに接続できていません");
    }
  }

}
