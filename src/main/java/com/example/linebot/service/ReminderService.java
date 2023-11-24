package com.example.linebot.service;

import com.example.linebot.replier.RemindOn;
import com.example.linebot.repository.ReminderRepository;
import com.example.linebot.value.ReminderItem;
import com.example.linebot.value.ReminderItemTuple;
import com.example.linebot.value.ReminderSlot;
import com.linecorp.bot.messaging.model.PushMessageRequest;
import com.linecorp.bot.messaging.model.TextMessage;
import com.linecorp.bot.webhook.model.MessageEvent;
import com.linecorp.bot.webhook.model.TextMessageContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReminderService {

  private final ReminderRepository repository;

  @Autowired
  public ReminderService(ReminderRepository reminderRepository) {
    this.repository = reminderRepository;
  }

  public RemindOn doReplyOfNewItem(MessageEvent event) {
    String userId = event.source().userId();
    if (event.message() instanceof TextMessageContent tmc) {
      String text = tmc.text();
      ReminderSlot slot = new ReminderSlot(text);
      ReminderItem item = new ReminderItem(userId, slot);

      repository.insert(item);

      return new RemindOn(text);
    }
    return new RemindOn(null);
  }

  public List<PushMessageRequest> doPushReminderItems() {
    List<ReminderItemTuple> previousItems = repository.findPreviousItems();
    List<PushMessageRequest> pushMessages = new ArrayList<>();
    // 本来であればUserIdごとにPushMessageをまとめるべきだが、授業レベルなので簡略化している
    for (ReminderItemTuple item : previousItems) {
      PushMessageRequest pushMessage = toPushMessage(item);
      pushMessages.add(pushMessage);
    }
    // 本来、Transactionalにすべきだが、教えてないので、次回実習で触る。
    // findPreviousItemsの後であればどこで実行されていてもとりあえず問題はない。
    repository.deletePreviousItems();
    return pushMessages;
  }

  private PushMessageRequest toPushMessage(ReminderItemTuple item) {
    String userId = item.getUserId();
    String pushText = item.getPushText();
    String body = String.format("%s の時間です！", pushText);
    return new PushMessageRequest.Builder(userId, List.of(new TextMessage(body))).build();
  }

}
