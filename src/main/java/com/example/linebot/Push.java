package com.example.linebot;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.ConfirmTemplate;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.model.richmenu.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class Push {

  private static final Logger log = LoggerFactory.getLogger(Push.class);

  // push先のユーザID（本来は、友達登録をした瞬間にDBなどに格納しておく）
  private String userId = "******";

  private final LineMessagingClient client;

  @Autowired
  public Push(LineMessagingClient lineMessagingClient) {
    this.client = lineMessagingClient;
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
    String text = DateTimeFormatter.ofPattern("a K:mm").format(LocalDateTime.now());
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

  // 確認メッセージをpush
  @GetMapping("confirm")
  public String pushConfirm() {
    String text = "質問だよ";
    try {
      Message msg = new TemplateMessage(text,
        new ConfirmTemplate("いいかんじ？",
          new PostbackAction("おけまる", "CY"),
          new PostbackAction("やばたん", "CN")));
      PushMessage pMsg = new PushMessage(userId, msg);
      BotApiResponse resp = client.pushMessage(pMsg).get();
      log.info("Sent messages: {}", resp);
    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    }
    return text;
  }

  @GetMapping("addRich")
  public String addRichMenu() {
    String text = "リッチメニュー追加だよ";

    RichMenu richMenu = RichMenu.builder()
      .name("name")
      .chatBarText("text")
      .areas(makeRichMenuAreas())
      .selected(true)
      .size(RichMenuSize.FULL)
      .build();

    try {
      RichMenuIdResponse resp1 = client.createRichMenu(richMenu).get();
      log.info("create richmenu:{}", resp1);

      ClassPathResource cpr = new ClassPathResource("/img/RichMenuSample.jpg");
      byte[] fileContent = Files.readAllBytes(cpr.getFile().toPath());
      BotApiResponse resp2 = client.setRichMenuImage(resp1.getRichMenuId(), "image/jpeg", fileContent).get();
      log.info("set richmenu image:{}", resp2);

      BotApiResponse resp3 = client.linkRichMenuIdToUser(userId, resp1.getRichMenuId()).get();
      log.info("link richmenu:{}", resp3);

    } catch (InterruptedException | ExecutionException | IOException e) {
      throw new RuntimeException(e);
    }
    return text;
  }

  @GetMapping("delRich")
  public String delRichMenu() {
    String text = "リッチメニュー削除だよ";

    try {

      RichMenuListResponse resp4 = client.getRichMenuList().get();
      log.info("get richmenus:{}", resp4);

      client.unlinkRichMenuIdFromUser(userId);

      resp4.getRichMenus().stream()
        .forEach(r -> {
          log.info(r.getRichMenuId());
          client.deleteRichMenu(r.getRichMenuId());
        });

    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    }
    return text;
  }

  private List<RichMenuArea> makeRichMenuAreas() {
    final ArrayList<RichMenuArea> richMenuAreas = new ArrayList<>();
    richMenuAreas.add(makeRichMenuArea(551, 325, 321, 321, "Up"));
    richMenuAreas.add(makeRichMenuArea(876, 651, 321, 321, "Right"));
    richMenuAreas.add(makeRichMenuArea(551, 972, 321, 321, "Down"));
    richMenuAreas.add(makeRichMenuArea(225, 651, 321, 321, "Left"));
    richMenuAreas.add(makeRichMenuArea(1433, 657, 367, 367, "b"));
    richMenuAreas.add(makeRichMenuArea(1907, 657, 367, 367, "a"));
    return richMenuAreas;
  }

  private RichMenuArea makeRichMenuArea(int x, int y, int w, int h, String name) {
    return new RichMenuArea(new RichMenuBounds(x, y, w, h),
      new MessageAction(name, name + "is tapped"));
  }

}
