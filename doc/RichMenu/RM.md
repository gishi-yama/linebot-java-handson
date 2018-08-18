## リッチメニューを作成・利用する

リッチメニューの詳細は[公式のドキュメント:リッチメニューを使う](https://developers.line.me/ja/docs/messaging-api/using-rich-menus/)を参照されたい。

リッチーメニューは、LINE@マネージャーから静的なメニューを作成する方法と、Messaging APIから動的なメニューを作成する方法がある。ここでは後者を紹介する。

### リッチメニューの画像を準備する

リッチメニューに使う画像を準備する。画像の仕様は[公式のドキュメント:リッチメニューの画像を準備する](https://developers.line.me/ja/docs/messaging-api/using-rich-menus/#prepare-a-rich-menu-image)を参照されたい。

今回は、公式のサンプル画像をダウンロードする。

![公式のサンプル画像](https://developers.line.me/media/messaging-api/rich-menu/controller-rich-menu-image-e1734c7d.jpg)

画像をダウンロードしたら、 `src/main/resources/img` フォルダに、 `RichMenuSample.jpg` という名称で保存する。

### RichMenuPushクラスを作成

一連の処理がわかりやすいように、RichMenuの管理（作成/表示/非表示）をする RichMenuController クラスを作成する。

` ******` のところは、Botをフォローしたときに教えてくれた自分のUserId（実際の開発時はデータベースなどから取得してくる）か、Line Developers サイトのBotの設定画面の一番下 `Your user ID` の値で書き換える。

```java
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
public class RichMenuController {

  private static final Logger log = LoggerFactory.getLogger(Push.class);

  // push先のユーザID（本来は、友達登録をした瞬間にDBなどに格納しておく）
  private String userId = "******";

  private final LineMessagingClient client;

  @Autowired
  public RichMenuController(LineMessagingClient lineMessagingClient) {
    this.client = lineMessagingClient;
  }

  // リッチーメニューを作成する
  @GetMapping("addRich")
  public String addRichMenu() {
    String text = "リッチメニューを作成し、ユーザーに紐付けます";

    // ①リッチメニューを作成
    // それぞれの意味は https://developers.line.me/ja/reference/messaging-api/#rich-menu-object を参照
    RichMenu richMenu = RichMenu.builder()
      .name("リッチメニュー1")
      .chatBarText("コントローラー")
      .areas(makeRichMenuAreas())
      .selected(true)
      .size(RichMenuSize.FULL)
      .build();

    try {

      // ②作成したリッチメニューの登録（ resp1 は作成結果で、リッチメニューIDが入っている）
      RichMenuIdResponse resp1 = client.createRichMenu(richMenu).get();
      log.info("create richmenu:{}", resp1);

      // ③リッチメニューの背景画像の設定( resp2 は、画像の登録結果）
      // ここでは、src/resource/img/RichMenuSample.jpg（ビルド後は classpath:/img/RichMenuSample.jpg）を指定
      // 画像の仕様は公式ドキュメントを参照されたい
      ClassPathResource cpr = new ClassPathResource("/img/RichMenuSample.jpg");
      byte[] fileContent = Files.readAllBytes(cpr.getFile().toPath());
      BotApiResponse resp2 = client.setRichMenuImage(resp1.getRichMenuId(), "image/jpeg", fileContent).get();
      log.info("set richmenu image:{}", resp2);

      // ④リッチメニューIdをユーザIdとリンクする（ resp3 は、紐付け結果）
      // リンクすることで作成したリッチメニューを使えるようになる
      BotApiResponse resp3 = client.linkRichMenuIdToUser(userId, resp1.getRichMenuId()).get();
      log.info("link richmenu:{}", resp3);

    } catch (InterruptedException | ExecutionException | IOException e) {
      throw new RuntimeException(e);
    }
    return text;
  }

  @GetMapping("delRich")
  public String delRichMenu() {
    String text = "リッチメニューをすべて削除します";
    try {

      // ①ユーザからリッチメニューを解除する（※Messaging APIで作成したものだけ）
      client.unlinkRichMenuIdFromUser(userId);

      // ②作成されているリッチメニューの取得（ resp4 は、リッチメニューの一覧情報）
      RichMenuListResponse resp4 = client.getRichMenuList().get();
      log.info("get richmenus:{}", resp4);

      // ③リッチメニューIdを指定して削除する
      // ここでは resp4 のものをすべて削除しているが、本来はリッチメニューIdと
      // ユーザIDの対応をDBなどに保存しておいて、不要なものだけを削除する
      resp4.getRichMenus().stream()
        .forEach(r -> client.deleteRichMenu(r.getRichMenuId()));

    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    }
    return text;
  }

  // 画像のどの部分（ピクセル）に、どんな動作をするリッチメニューを割り当てるか設定します
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

  // Botにメッセージを送信する動作をリッチメニューとして割り当てます
  private RichMenuArea makeMessageAction(int x, int y, int w, int h, String label) {
    return new RichMenuArea(new RichMenuBounds(x, y, w, h),
      new MessageAction(label, label + "　is tapped！"));
  }

  // アプリ内ブラウザでWebサイトを表示する動作をリッチメニューとして割り当てます
  private RichMenuArea makeURIAction(int x, int y, int w, int h, String label, String uri) {
    return new RichMenuArea(new RichMenuBounds(x, y, w, h),
      new URIAction(label, uri));
  }

  // Botに日時イベントを送信する動作をリッチメニューとして割り当てます
  private RichMenuArea makeDateTimeAction(int x, int y, int w, int h, String label) {
    return new RichMenuArea(new RichMenuBounds(x, y, w, h),
      new DatetimePickerAction(label, "DT", "datetime"));
  }

}
```

### Callback クラスを変更

RichMenuController の最後のメソッドで、 `DatetimePickerAction` を利用している。

これは `PostbackAction` と同様に `PostBackEvent` をBotに送信するので、対応する Callback#handlePostBack メソッドを変更しておく。

```java
  // PostBackEventに対応する
  @EventMapping
  public Message handlePostBack(PostbackEvent event) {
    String actionLabel = event.getPostbackContent().getData();
    switch (actionLabel) {
      case "CY":
        return reply("イイね！");
      case "CN":
        return reply("つらたん");
      // ---------- ここから変更 -----------
      case "DT":
        return reply(event.getPostbackContent().getParams().toString());
      // ---------- ここまで変更 -----------
    }
    return reply("?");
  }
```

### 動作確認

必ずスマートフォン（タブレット）のLINEアプリで動作確認してください（本稿執筆時点でPC版は対応していない）

1. LineBotApplication を一度停止して、再起動する
2. [http://localhost:8080/addRich](http://localhost:8080/addRich) にブラウザでアクセスする
3. Botのメッセージ欄に、「コントローラー」（chatBarTextに設定した文字列）のリッチメニューができていることを確認する
<br>追加前：![リッチメニュー追加前](./RM01.jpg)
<br>追加後：![リッチメニュー追加後](./RM02.jpg)
4. 「コントローラー」を押すと、リッチメニューに設定した画像（RichMenuSample.jpg）が表示される。
<br>![リッチメニュー表示](./RM03.jpg)
5. リッチメニューの緑の十時ボタンを押すと、その方向をBotにメッセージで送る
<br>（このハンズオンのBotは、予め定められたメッセージ以外はオウム返しするので、方向をオウム返しする）
<br>![十字ボタンでオウム返し](./RM04.jpg)
6. 赤い左ボタンを押すと、Webサイト（今回は、google）を表示する
<br>![赤左ボタンでWebサイト表示](./RM05.jpg)
7. 赤い右ボタンを押すと、日時の選択メニューが表示される
<br>日時を送信すると Callback#handlePostBack メソッドで変更したとおり、選択された日時をBotがパラメータとして受信していることを表示する
<br>![赤右ボタンで尾日付を選択](./RM06.jpg)<br>
<br>![選んだ日時をパラメータで受信](./RM07.jpg)
8. [http://localhost:8080/delRich](http://localhost:8080/delRich) にブラウザでアクセスすると、リッチメニューが無くなる

### 解説と補足

本稿執筆時点で、以下のような状態である。

- Line@マネージャで作成・リンクしたメニューと、Message APIで作成したメニューは、後者の方が優先される
- Line@マネージャで作成・リンクしたメニューを、Message APIでは管理できない（リッチメニューIdが取得できない）
- Line@マネージャでは、アイコンだけでリッチメニューを作成できるが、Message APIでは画像つきのものしか作成できない

-----

[戻る](../../README.md)
