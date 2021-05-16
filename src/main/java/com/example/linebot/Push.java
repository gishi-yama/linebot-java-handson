package com.example.linebot;

import com.example.linebot.service.ReminderService;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.ConfirmTemplate;
import com.linecorp.bot.model.response.BotApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class Push {

  private static final Logger log = LoggerFactory.getLogger(Push.class);

  // push先のユーザID（本来は、友達登録をした瞬間にDBなどに格納しておく）
  private String userId = "******";

  private final LineMessagingClient messagingClient;

  private final ReminderService reminderService;

  @Autowired
  public Push(LineMessagingClient lineMessagingClient, ReminderService reminderService) {
    this.messagingClient = lineMessagingClient;
    this.reminderService = reminderService;
  }

  // テスト
  @GetMapping("test")
  public String hello(HttpServletRequest request) {
    return "Get from " + request.getRequestURL();
  }

  // 時報をpushする
  // */1は1分ごとの意味。5に変えれば5分ごととかになる。
  @GetMapping("timetone")
//  @Scheduled(cron = "0 */1 * * * *", zone = "Asia/Tokyo")
  public String pushTimeTone() {
    var text = DateTimeFormatter.ofPattern("a K:mm").format(LocalDateTime.now());
    try {
      var pMsg
        = new PushMessage(userId, new TextMessage(text));
      var resp = messagingClient.pushMessage(pMsg).get();
      log.info("Sent messages: {}", resp);
    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    }
    return text;
  }

  @Scheduled(cron = "0 */1 * * * *", zone = "Asia/Tokyo")
  public void pushReminder() {
    try {
      List<PushMessage> pushMessages = reminderService.doPushReminderItems();
      for (PushMessage pushMessage : pushMessages) {
        BotApiResponse resp = messagingClient.pushMessage(pushMessage).get();
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
          new PostbackAction("おけまる", "CY"),
          new PostbackAction("やばたん", "CN")));
      var pMsg = new PushMessage(userId, msg);
      var resp = messagingClient.pushMessage(pMsg).get();
      log.info("Sent messages: {}", resp);
    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    }
    return text;
  }

}
