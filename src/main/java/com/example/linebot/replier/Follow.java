package com.example.linebot.replier;


import com.linecorp.bot.messaging.model.Message;
import com.linecorp.bot.messaging.model.TextMessage;
import com.linecorp.bot.webhook.model.FollowEvent;

// フォローされた時用の返信クラス
public class Follow implements Replier {

  private FollowEvent event;

  public Follow(FollowEvent event) {
    this.event = event;
  }

  @Override
  public Message reply() {
    String userId = this.event.source().userId();
    String text = String.format("あなたのユーザーID:%s", userId);
    return new TextMessage(text);
  }
}
