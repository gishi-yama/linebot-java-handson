package com.example.linebot.service;

import com.example.linebot.replier.RemindOn;
import com.example.linebot.repository.ReminderRepository;
import com.example.linebot.value.ReminderItem;
import com.example.linebot.value.ReminderSlot;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RemainderService {

  private final ReminderRepository repository;

  @Autowired
  public RemainderService(ReminderRepository reminderRepository) {
    this.repository = reminderRepository;
  }

  public RemindOn doReplyOfNewItem(MessageEvent<TextMessageContent> event) {
    String userId = event.getSource().getUserId();
    TextMessageContent tmc = event.getMessage();
    String text = tmc.getText();
    ReminderSlot slot = new ReminderSlot(text);
    ReminderItem item = new ReminderItem(userId, slot);

    repository.insert(item);

    return new RemindOn(text);
  }

}
