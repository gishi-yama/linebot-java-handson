package com.example.linebot;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.ConfirmTemplate;
import com.linecorp.bot.model.response.BotApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutionException;

@RestController
public class Push {

  private static final Logger log = LoggerFactory.getLogger(Push.class);

  // 返答から拾ってくる（本来は、友達登録をした瞬間にDBなどに格納しておく）
  private String userId = "ユーザId";

  private final LineMessagingClient client;

  public Push(LineMessagingClient lineMessagingClient) {
    this.client = lineMessagingClient;
  }

  // テスト
  @GetMapping("test")
  public String hello() {
    return "Hello!";
  }

  //リマインドをプッシュ
  @GetMapping("push1")
  public String pushMessage1() {
    String text = DateTimeFormatter.ofPattern("a K:k").format(LocalDateTime.now());
    try {
      PushMessage pMsg
        = new PushMessage(userId, new TextMessage(text));
      BotApiResponse resp = client.pushMessage(pMsg).get();
      log.info("Sent messages: {}", resp);
    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    }
    return text;
  }

  //リマインドをプッシュ
  @GetMapping("push2")
  public String pushMessage2() {
    String text = "質問だよ";
    try {
      Message msg = new TemplateMessage(text,
        new ConfirmTemplate("おけまる？",
          new PostbackAction("おけまる。", "QY"),
          new PostbackAction("やばたに。", "QN")));
      PushMessage pMsg = new PushMessage(userId, msg);
      BotApiResponse resp = client.pushMessage(pMsg).get();
      log.info("Sent messages: {}", resp);
    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    }
    return text;
  }

}
