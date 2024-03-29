package com.example.linebot;

import com.example.linebot.replier.*;
import com.example.linebot.service.ExternalService;
import com.example.linebot.service.ReminderService;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


@LineMessageHandler
public class Callback {

  private static final Logger log = LoggerFactory.getLogger(Callback.class);

  private final ReminderService remainderService;
  private final ExternalService externalService;

  @Autowired
  public Callback(ReminderService remainderService, ExternalService externalService) {
    this.remainderService = remainderService;
    this.externalService = externalService;
  }

  // フォローイベントに対応する
  @EventMapping
  public Message handleFollow(FollowEvent event) {
    // 実際はこのタイミングでフォロワーのユーザIDをデータベースにに格納しておくなど
    Follow follow = new Follow(event);
    return follow.reply();
  }

  // 文章で話しかけられたとき（テキストメッセージのイベント）に対応する
  @EventMapping
  public Message handleMessage(MessageEvent<TextMessageContent> event) {
    TextMessageContent tmc = event.getMessage();
    String text = tmc.getText();
    switch (Intent.whichIntent(text)) {
      case REMINDER:
        RemindOn reminderOn = remainderService.doReplyOfNewItem(event);
        return reminderOn.reply();
      case PYTHON_GREET:
        PythonGreet pythonGreet = externalService.doReplyWithPython();
        return pythonGreet.reply();
      case COVID_REPORT:
        CovidReport covidReport = externalService.doReplyWithCovid(text);
        return covidReport.reply();
      case UNKNOWN:
      default:
        Parrot parrot = new Parrot(event);
        return parrot.reply();
    }

//    switch (text) {
//      case "やあ":
//        Greet greet = new Greet();
//        return greet.reply();
//      case "おみくじ":
//        Omikuji omikuji = new Omikuji();
//        return omikuji.reply();
//      case "バブル":
//        BubbleSample bubbleSample = new BubbleSample();
//        return bubbleSample.reply();
//      case "カルーセル":
//        CarouselSample carouselSample = new CarouselSample();
//        return carouselSample.reply();
//      case "クイックリプライ":
//        QRFunctions qrFunctions = new QRFunctions();
//        return qrFunctions.reply();
//      case "部屋":
//        RoomInfo roomInfo = new RoomInfo();
//        return roomInfo.reply();
//      default:
//        Parrot parrot = new Parrot(event);
//        return parrot.reply();
//    }
  }

}
