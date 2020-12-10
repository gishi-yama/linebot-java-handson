package com.example.linebot.replier;

import com.example.linebot.CO2;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.HttpClientErrorException;

import java.net.URI;

public class RoomInfo implements Replier {

  @Override
  public Message reply() {
    var key = "******";
    var url = "https://us.wio.seeed.io/v1/node/GroveCo2MhZ16UART0/concentration_and_temperature?access_token=";
    var uri = URI.create(url + key);
    var restTemplate = new RestTemplateBuilder().build();
    try {
      CO2 co2 = restTemplate.getForObject(uri, CO2.class);
      String text = String.format("二酸化炭素は %f ppm、温度は %f 度です",
        co2.getConcentration(), co2.getTemperature());
      return new TextMessage(text);
    } catch (HttpClientErrorException e) {
      e.printStackTrace();
      return new TextMessage("センサーに接続できていません");
    }
  }
}
