package com.example.linebot.replier;

import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

public class DialogAnswer implements Replier {

  private PostbackEvent event;

  public DialogAnswer(PostbackEvent event) {
    this.event = event;
  }

  @Override
  public Message reply() {
    String actionLabel = this.event.getPostbackContent().getData();
    switch (actionLabel) {
      case "CY":
        return new TextMessage("イイね！");
      case "CN":
        return new TextMessage("つらたん");
      case "DT":
        return new TextMessage(this.event.getPostbackContent().getParams().toString());
    }
    return new TextMessage("?");
  }
}
