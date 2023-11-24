package com.example.linebot.replier;


import com.linecorp.bot.messaging.model.Message;

import java.util.Arrays;

public class QRFunctions implements Replier {
  @Override
  public Message reply() {
    return null;
  }
//
//  @Override
//  public Message reply() {
//    var items = Arrays.asList(
//      QuickReplyItem.builder()
//        .action(new MessageAction("メッセージ", "メッセージ"))
//        .build(),
//      QuickReplyItem.builder()
//        .action(CameraAction.withLabel("カメラ"))
//        .build(),
//      QuickReplyItem.builder()
//        .action(CameraRollAction.withLabel("カメラロール"))
//        .build(),
//      QuickReplyItem.builder()
//        .action(LocationAction.withLabel("位置情報"))
//        .build(),
//      QuickReplyItem.builder()
//        .action(PostbackAction.builder()
//          .label("PostbackAction")
//          .text("PostbackAction clicked")
//          .data("{PostbackAction: true}")
//          .build())
//        .build()
//    );
//
//    var quickReply = QuickReply.items(items);
//
//    return TextMessage
//      .builder()
//      .text("Message with QuickReply")
//      .quickReply(quickReply)
//      .build();
//  }
}
