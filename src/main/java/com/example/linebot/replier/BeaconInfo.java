package com.example.linebot.replier;


import com.linecorp.bot.messaging.model.Message;
import com.linecorp.bot.messaging.model.TextMessage;
import com.linecorp.bot.webhook.model.BeaconEvent;

public class BeaconInfo implements Replier {

  private BeaconEvent event;

  public BeaconInfo(BeaconEvent event) {
    this.event = event;
  }

  @Override
  public Message reply() {
    // Beaconイベントの内容を文字列に変換する
    var eventStr = event.beacon().toString();
    // eventStr をBotで返信する
    return new TextMessage(eventStr);
  }
}
