package com.example.linebot.service;

import com.example.linebot.replier.RemindOn;
import com.example.linebot.value.ReminderItem;
import com.example.linebot.value.ReminderSlot;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import org.springframework.stereotype.Service;

@Service
public class RemainderService {

  public RemindOn doReplyOfNewItem(MessageEvent<TextMessageContent> event) {
    String userId = event.getSource().getUserId();
    TextMessageContent tmc = event.getMessage();
    String text = tmc.getText();
    ReminderSlot slot = new ReminderSlot(text);
    ReminderItem item = new ReminderItem(userId, slot);


    return new RemindOn(text);
  }


}
