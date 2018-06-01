package com.example.linebot;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalTime;

@LineMessageHandler
public class Callback {

  private static final Logger log = LoggerFactory.getLogger(Push.class);

  // MessageEventに対応する
  @EventMapping
  public TextMessage handleMessage(MessageEvent<TextMessageContent> event) {
    TextMessageContent tmc = event.getMessage();
    String text = tmc.getText();
    switch (text) {
      case "こんにちは":
      case "おはよう":
      case "こんばんは":
        return greet();
      case "教えて":
        return replyUserId(event);
      case "教室":
        return makeRoomInfo();
      default:
        return parrot(text);
    }
  }

  // マッピングされていないEventに対応する
  @EventMapping
  public void handleEvent(Event event) {
    System.out.println("event: " + event);
  }

  // オウム返しをする
  private TextMessage parrot(String text) {
    return new TextMessage(text);
  }

  // あいさつする
  private TextMessage greet() {
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

  // MessageEventからuserIdを取り出して返答する　
  private TextMessage replyUserId(MessageEvent<TextMessageContent> event) {
    String userId = event.getSource().getUserId();
    return new TextMessage("あなたのユーザIDは " + userId);
  }

  // センサーの値をWebから取得して、CO2クラスのインスタンスにいれる(******の所は、別途指示します）
  private TextMessage makeRoomInfo() {
    String key = "******";
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

  // PostBackEventに対応する
  @EventMapping
  public TextMessage handlePostBack(PostbackEvent event) {
    String actionLabel = event.getPostbackContent().getData();
    switch (actionLabel) {
      case "CY":
        return new TextMessage("いいね！");
      case "CN":
        return new TextMessage("つらたん...");
      default:
        return new TextMessage("？");
    }
  }

}
