package com.example.linebot;

import com.example.linebot.replier.*;
import com.linecorp.bot.client.LineBlobClient;
import com.linecorp.bot.client.MessageContentResponse;
import com.linecorp.bot.model.event.*;
import com.linecorp.bot.model.event.message.ImageMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;


@LineMessageHandler
public class Callback {

  private static final Logger log = LoggerFactory.getLogger(Callback.class);

  private final LineBlobClient blobClient;

  @Autowired
  public Callback(LineBlobClient blobClient) {
    this.blobClient = blobClient;
  }

  // マッピングされていないEventに対応する
  @EventMapping
  public void handleEvent(Event event) {
    System.out.println("event: " + event);
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
    switch (Intent.makeIntent(text)) {
      case REMINDER:
        return new TextMessage("リマインダーです");
      case UNKNOWN:
        Parrot parrot = new Parrot(event);
        return parrot.reply();
    }

    switch (text) {
      case "やあ":
        Greet greet = new Greet();
        return greet.reply();
      case "おみくじ":
        Omikuji omikuji = new Omikuji();
        return omikuji.reply();
      case "バブル":
        BubbleSample bubbleSample = new BubbleSample();
        return bubbleSample.reply();
      case "カルーセル":
        CarouselSample carouselSample = new CarouselSample();
        return carouselSample.reply();
      case "クイックリプライ":
        QRFunctions qrFunctions = new QRFunctions();
        return qrFunctions.reply();
      case "部屋":
        RoomInfo roomInfo = new RoomInfo();
        return roomInfo.reply();
      default:
        Parrot parrot = new Parrot(event);
        return parrot.reply();
    }
  }

  // PostBackEventに対応する
  @EventMapping
  public Message handlePostBack(PostbackEvent event) {
    DialogAnswer dialogAnswer = new DialogAnswer(event);
    return dialogAnswer.reply();
  }

  // 画像のメッセージイベントに対応する
  @EventMapping
  public Message handleImg(MessageEvent<ImageMessageContent> event) {
    // 画像メッセージのidを取得する
    String msgId = event.getMessage().getId();
    Optional<MessageContentResponse> resp = getContentResponse(msgId);
    return resp
      .map(HandleImage::new)
      .map(HandleImage::reply)
      .orElseGet(() -> new TextMessage("ファイル読み込みNG"));
  }

  public Optional<MessageContentResponse> getContentResponse(String msgId) {
    //  ②メッセージのidを使って MessageContentResponse を取得する
    try (MessageContentResponse resp = blobClient.getMessageContent(msgId).get()) {
      log.info("get content{}:", resp);
      return Optional.of(resp);
    } catch (InterruptedException | ExecutionException | IOException e) {
      e.printStackTrace();
    }
    return Optional.empty();
  }

  // BeaconEventに対応する
  @EventMapping
  public Message handleBeacon(BeaconEvent event) {
    BeaconInfo beaconInfo = new BeaconInfo(event);
    return beaconInfo.reply();
  }

}
