package com.example.linebot;

import com.example.linebot.service.ReminderService;
import com.linecorp.bot.messaging.client.MessagingApiClient;
import com.linecorp.bot.messaging.model.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
public class Push {

  private static final Logger log = LoggerFactory.getLogger(Push.class);

  // push先のユーザID（本来は、友達登録をした瞬間にDBなどに格納しておく）
  private String userId = "******";

  private final MessagingApiClient messagingApiClient;

  private final ReminderService reminderService;

  @Autowired
  public Push(MessagingApiClient messagingApiClient, ReminderService reminderService) {
    this.messagingApiClient = messagingApiClient;
    this.reminderService = reminderService;
  }

  // テスト
  @GetMapping("test")
  public String hello(HttpServletRequest request) {
    return "Get from " + request.getRequestURL();
  }


//  if (event.message() instanceof TextMessageContent) {
//    TextMessageContent message = (TextMessageContent) event.message();
//    final String originalMessageText = message.text();
//    messagingApiClient.replyMessage(new ReplyMessageRequest(
//      event.replyToken(),
//      List.of(new TextMessage(originalMessageText)),
//      false));
//  }

  // 時報をpushする
  // */1は1分ごとの意味。5に変えれば5分ごととかになる。
  @GetMapping("timetone")
//  @Scheduled(cron = "0 */1 * * * *", zone = "Asia/Tokyo")
  public String pushTimeTone() {
    var text = DateTimeFormatter.ofPattern("a K:mm").format(LocalDateTime.now());
    try {
      var pMsgs
        = new PushMessageRequest.Builder(userId, List.of(new TextMessage(text))).build();
      var resp = messagingApiClient.pushMessage(UUID.randomUUID(), pMsgs).get();
      log.info("Sent messages: {}", resp);
    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    }
    return text;
  }

  @Scheduled(cron = "0 */1 * * * *", zone = "Asia/Tokyo")
  public void pushReminder() {
    try {
      List<PushMessageRequest> pMsgs = reminderService.doPushReminderItems();
      for (PushMessageRequest pMsg : pMsgs) {
        var resp = messagingApiClient.pushMessage(UUID.randomUUID(), pMsg).get();
        log.info("Sent messages: {}", resp);
      }
    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    }
  }

  // 確認メッセージをpush
  @GetMapping("confirm")
  public String pushConfirm() {
    var text = "質問だよ";
    try {
      var msg = new TemplateMessage(text,
        new ConfirmTemplate("いいかんじ？",
          List.of(
          new PostbackAction.Builder().label("おけまる").data("CY").build(),
          new PostbackAction.Builder().label("やばたん").data("CN").build()
          )));
      var pMsg = new PushMessageRequest.Builder(userId, List.of(msg)).build();
      var resp = messagingApiClient.pushMessage(UUID.randomUUID(), pMsg).get();
      log.info("Sent messages: {}", resp);
    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    }
    return text;
  }

}
