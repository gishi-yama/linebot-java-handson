package com.example.linebot;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.ConfirmTemplate;
import com.linecorp.bot.model.response.BotApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

@RestController
public class Push {

  private static final Logger log = LoggerFactory.getLogger(Push.class);

  private final LineMessagingClient client;

  // 返答から拾ってくる（本来は、友達登録をした瞬間にDBなどに格納しておく）
  private String userId = "Uffeb67684a6130234169cdbb5111957d";

  public Push(LineMessagingClient lineMessagingClient) {
    this.client = lineMessagingClient;
  }

  //リマインドをプッシュ
  @GetMapping("push")
  public void pushAlarm() throws URISyntaxException {

    try {
      BotApiResponse response = client
        .pushMessage(new PushMessage("Uffeb67684a6130234169cdbb5111957d",
          new TextMessage("明日は燃えるごみの日だよ！")))
        .get();
      log.info("Sent messages: {}", response);
    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    }
  }

}
