package com.example.linebot;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.FollowEvent;
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

  // マッピングされていないEventに対応する
  @EventMapping
  public void handleEvent(Event event) {
    System.out.println("event: " + event);
  }

  @EventMapping
  public TextMessage handleFollow(FollowEvent event) {
    // 実際はこのタイミングでフォロワーのユーザIDをデータベースにに格納しておくなど
    String userId = event.getSource().getUserId();
    return reply("あなたのユーザIDは " + userId);
  }

  // TextMessageに対応する
  @EventMapping
  public TextMessage handleMessage(MessageEvent<TextMessageContent> event) {
    TextMessageContent tmc = event.getMessage();
    String text = tmc.getText();
    switch (text) {
      case "やあ":
        return greet();
      case "部屋":
        return replyRoomInfo();
      default:
        return reply(text);
    }
  }

  // 返答メッセージを作る
  private TextMessage reply(String text) {
    return new TextMessage(text);
  }

  // あいさつする
  private TextMessage greet() {
    LocalTime lt = LocalTime.now();
    int hour = lt.getHour();
    if (hour >= 17) {
      return reply("こんばんは！");
    }
    if (hour >= 11) {
      return reply("こんにちは！");
    }
    return reply("おはよう！");
  }

  // センサーの値をWebから取得して、CO2クラスのインスタンスにいれる(******の所は、別途指示します）
  private TextMessage replyRoomInfo() {
    String key = "******";
    String url = "https://us.wio.seeed.io/v1/node/GroveCo2MhZ16UART0/concentration_and_temperature?access_token=";
    URI uri = URI.create(url + key);
    RestTemplate restTemplate = new RestTemplateBuilder().build();
    try {
      CO2 co2 = restTemplate.getForObject(uri, CO2.class);
      return reply("二酸化炭素は" + co2.getConcentration() + "ppm、温度は" + co2.getTemperature() + "度です");
    } catch (HttpClientErrorException e) {
      e.printStackTrace();
      return reply("センサーに接続できていません");
    }
  }

  // PostBackEventに対応する
  @EventMapping
  public TextMessage handlePostBack(PostbackEvent event) {
    String actionLabel = event.getPostbackContent().getData();
    switch (actionLabel) {
      case "CY":
        return reply("イイね！");
      case "CN":
        return reply("つらたん");
    }
    return reply("?");
  }

}
