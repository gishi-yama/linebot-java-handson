package com.example.linebot.replier;

import com.linecorp.bot.model.event.BeaconEvent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

public class BeaconInfo implements Replier {

  private BeaconEvent event;

  public BeaconInfo(BeaconEvent event) {
    this.event = event;
  }

  @Override
  public Message reply() {
    // Beaconイベントの内容を文字列に変換する
    var eventStr = event.getBeacon().toString();
    // eventStr をBotで返信する
    return new TextMessage(eventStr);
  }
}
