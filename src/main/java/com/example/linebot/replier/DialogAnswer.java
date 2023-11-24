package com.example.linebot.replier;


import com.linecorp.bot.messaging.model.Message;
import com.linecorp.bot.messaging.model.TextMessage;
import com.linecorp.bot.webhook.model.PostbackEvent;

public class DialogAnswer implements Replier {

  private PostbackEvent event;

  public DialogAnswer(PostbackEvent event) {
    this.event = event;
  }

  @Override
  public Message reply() {
    String actionLabel = this.event.postback().data();
    switch (actionLabel) {
      case "CY":
        return new TextMessage("イイね！");
      case "CN":
        return new TextMessage("つらたん");
      case "DT":
        return new TextMessage(this.event.postback().params().toString());
    }
    return new TextMessage("?");
  }
}
