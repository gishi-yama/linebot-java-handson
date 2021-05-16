package com.example.linebot.service;

import com.example.linebot.replier.RemindOn;
import com.example.linebot.repository.ReminderRepository;
import com.example.linebot.value.ReminderItem;
import com.example.linebot.value.ReminderSlot;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReminderService {

  private final ReminderRepository repository;

  @Autowired
  public ReminderService(ReminderRepository reminderRepository) {
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

  public List<PushMessage> doPushReminderItems() {
    List<ReminderItem> previousItems = repository.findPreviousItem();
    List<PushMessage> pushMessages = new ArrayList<>();
    // 本来であればUserIdごとにPushMessageをまとめるべきだが、授業レベルなので簡略化している
    for (ReminderItem item : previousItems) {
      PushMessage pushMessage = toPushMessage(item);
      pushMessages.add(pushMessage);
    }
    return pushMessages;
  }

  private PushMessage toPushMessage(ReminderItem item) {
    String userId = item.getUserId();
    String pushText = item.getSlot().getPushText();
    String body = String.format("%s の時間です！", pushText);
    return new PushMessage(userId, new TextMessage(body));
  }

}
