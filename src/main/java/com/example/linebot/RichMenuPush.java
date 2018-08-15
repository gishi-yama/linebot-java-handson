package com.example.linebot;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.action.DatetimePickerAction;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.model.richmenu.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class RichMenuPush {

  private static final Logger log = LoggerFactory.getLogger(Push.class);

  // push先のユーザID（本来は、友達登録をした瞬間にDBなどに格納しておく）
  private String userId = "******";

  private final LineMessagingClient client;

  @Autowired
  public RichMenuPush(LineMessagingClient lineMessagingClient) {
    this.client = lineMessagingClient;
  }

  @GetMapping("addRich")
  public String addRichMenu() {
    String text = "リッチメニュー追加だよ";

    RichMenu richMenu = RichMenu.builder()
      .name("リッチメニュー1")
      .chatBarText("コントローラー")
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
    richMenuAreas.add(makeMessageAction(551, 325, 321, 321, "Up"));
    richMenuAreas.add(makeMessageAction(876, 651, 321, 321, "Right"));
    richMenuAreas.add(makeMessageAction(551, 972, 321, 321, "Down"));
    richMenuAreas.add(makeMessageAction(225, 651, 321, 321, "Left"));
    richMenuAreas.add(makeURIAction(1433, 657, 367, 367, "a", "https://google.com/"));
    richMenuAreas.add(makeDateTimeAction(1907, 657, 367, 367, "b"));
    return richMenuAreas;
  }

  private RichMenuArea makeMessageAction(int x, int y, int w, int h, String label) {
    return new RichMenuArea(new RichMenuBounds(x, y, w, h),
      new MessageAction(label, label + "is tapped"));
  }

  private RichMenuArea makeURIAction(int x, int y, int w, int h, String label, String uri) {
    return new RichMenuArea(new RichMenuBounds(x, y, w, h),
      new URIAction(label, uri));
  }

  private RichMenuArea makeDateTimeAction(int x, int y, int w, int h, String label) {
    return new RichMenuArea(new RichMenuBounds(x, y, w, h),
      new DatetimePickerAction(label, "DT", "datetime"));
  }

}
