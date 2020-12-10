## Flex Message を使う

Flex Message は、コンポーネントベースでBotの吹き出し（「バブル」という）を作成できる。

- [公式ドキュメント：Flex Messageを使う](https://developers.line.me/ja/docs/messaging-api/using-flex-messages/)
- [公式ドキュメント：Flex Messageの要素](https://developers.line.me/ja/docs/messaging-api/flex-message-elements/)

例えば、写真と説明文とボタンを一つのバブルにまとめて表示したり、複数のバブルを横スクロールで表示する（「カルーセル」という）こともできる。

### バブルを表示する

#### BubbleSample クラスを追加

```java
package com.example.linebot.replier;

import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.Separator;
import com.linecorp.bot.model.message.flex.component.Text;
import com.linecorp.bot.model.message.flex.unit.FlexAlign;
import com.linecorp.bot.model.message.flex.unit.FlexFontSize;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;

import java.util.Arrays;

public class BubbleSample implements Replier {

  @Override
  public Message reply() {
    var hello = Text.builder()
      .text("Hello")
      .build();

    var world = Text.builder()
      .text("world")
      .weight(Text.TextWeight.BOLD)
      .size(FlexFontSize.XL)
      .align(FlexAlign.CENTER)
      .color("#FF0000")
      .build();

    var separator = Separator.builder().build();

    var box = Box.builder()
      .layout(FlexLayout.HORIZONTAL)
      .contents(Arrays.asList(hello, separator, world))
      .build();

    var bubble = com.linecorp.bot.model.message.flex.container.Bubble.builder()
      .body(box)
      .build();

    return new FlexMessage("BubbleSample", bubble);
  }
  
}
```

#### Collback クラスを変更

以下を import に追加する（同名のクラスが多いので注意）。

```java
import com.example.linebot.replier.BubbleSample;
```

handleMessage メソッドの中身を修正する。

```java
@LineMessageHandler
public class Callback {

  // ------------ 中略・変更なし ------------
  
  // 文章で話しかけられたとき（テキストメッセージのイベント）に対応する
  @EventMapping
  public Message handleMessage(MessageEvent<TextMessageContent> event) {
    TextMessageContent tmc = event.getMessage();
    String text = tmc.getText();
    switch (text) {
      case "やあ":
        Greet greet = new Greet();
        return greet.reply();
      case "おみくじ":
        Omikuji omikuji = new Omikuji();
        return omikuji.reply();
      case "バブル":
        BubbleSample bubbleSample = new BubbleSample();
        return bubbleSample.reply();
      default:
        Parrot parrot = new Parrot(event);
        return parrot.reply();
    }
  }

  // ------------ 中略・変更なし ------------ 
 
}
```

#### 動作確認

1. LineBotApplication を一度停止して、再起動する
2. LINEアプリで、Botに向かって `バブル` と送信する
3. Botが整形されたFlexメッセージを表示する<br>![Flex01](Flex01.png)

### カルーセルを表示する

#### CarouselSample クラスを作成する

```java
package com.example.linebot.replier;

import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.Button;
import com.linecorp.bot.model.message.flex.component.Image;
import com.linecorp.bot.model.message.flex.component.Text;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.container.Carousel;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;

import java.net.URI;
import java.util.Arrays;

public class CarouselSample implements Replier {

  @Override
  public Message reply() {
    var currentTitle = Text.builder()
      .text("今日のイベントはこちら")
      .build();

    var currentHeader = Box.builder()
      .layout(FlexLayout.VERTICAL)
      .contents(Arrays.asList(currentTitle))
      .build();

    var currentImage = Image.builder()
      .url(URI.create("https://connpass-tokyo.s3.amazonaws.com/thumbs/3e/b8/3eb8be3f66515598c47c76bd65e3ebb2.png"))
      .size(Image.ImageSize.FULL_WIDTH)
      .aspectMode(Image.ImageAspectMode.Fit)
      .build();

    var currentText = Text.builder()
      .text("LINE Messaging API for Java でLINE Botを作ってみませんか？\n" +
        "エントリーを考えている方・考えていない方、社会人、学生の皆さん、誰でも大歓迎です！")
      .wrap(true)
      .build();

    var currentBtn = Button.builder()
      .style(Button.ButtonStyle.SECONDARY)
      .action(new URIAction("表示",
        URI.create("https://javado.connpass.com/event/97107/"),
        new URIAction.AltUri(URI.create("https://javado.connpass.com/event/97107/"))))
      .build();

    var currentBody = Box.builder()
      .layout(FlexLayout.VERTICAL)
      .contents(Arrays.asList(currentText, currentBtn))
      .build();

    var currentBbl = Bubble.builder()
      .header(currentHeader)
      .hero(currentImage)
      .body(currentBody)
      .build();

    var nextTitle = Text.builder()
      .text("次回のイベントはこちら")
      .build();

    var nextHeader = Box.builder()
      .layout(FlexLayout.VERTICAL)
      .contents(Arrays.asList(nextTitle))
      .build();

    var nextImage = Image.builder()
      .url(URI.create("https://connpass-tokyo.s3.amazonaws.com/thumbs/9a/82/9a82ae80521b1f119cc6ed1e3e5edac0.png"))
      .size(Image.ImageSize.FULL_WIDTH)
      .aspectMode(Image.ImageAspectMode.Fit)
      .build();

    var nextText = Text.builder()
      .text("待ちに待ったスキルの開発環境・CEK(Clova Extension Kit)がお目見えしました!!\n" +
        "Clovaスキルを作ってみたい！Clovaと触れ合いたい！とお考えの皆さんのためにCEKのハンズオンを行います。")
      .wrap(true)
      .build();

    var nextBtn = Button.builder()
      .style(Button.ButtonStyle.PRIMARY)
      .action(new URIAction("申し込み",
        URI.create("https://linedev.connpass.com/event/96793/"),
        new URIAction.AltUri(URI.create("https://linedev.connpass.com/event/96793/"))))
      .build();

    var nextBody = Box.builder()
      .layout(FlexLayout.VERTICAL)
      .contents(Arrays.asList(nextText, nextBtn))
      .build();

    var nextBbl = Bubble.builder()
      .header(nextHeader)
      .hero(nextImage)
      .body(nextBody)
      .build();

    var carousel = Carousel.builder()
      .contents(Arrays.asList(currentBbl, nextBbl))
      .build();

    return new FlexMessage("カルーセル", carousel);
  }
}
```


#### Collback クラスを変更

必要に応じて、以下を import に追加する（同名のクラスが多いので注意）。

以下を import に追加する（同名のクラスが多いので注意）。

```java
import com.example.linebot.replier.CarouselSample;
```

handleMessage メソッドの中身を修正する。

```java
@LineMessageHandler
public class Callback {

  // ------------ 中略・変更なし ------------
  
  // 文章で話しかけられたとき（テキストメッセージのイベント）に対応する
  @EventMapping
  public Message handleMessage(MessageEvent<TextMessageContent> event) {
    TextMessageContent tmc = event.getMessage();
    String text = tmc.getText();
    switch (text) {
       case "やあ":
          Greet greet = new Greet();
          return greet.reply();
       case "おみくじ":
          Omikuji omikuji = new Omikuji();
          return omikuji.reply();
       case "バブル":
          BubbleSample bubbleSample = new BubbleSample();
          return bubbleSample.reply();
       case "カルーセル":
          CarouselSample carouselSample = new CarouselSample();
          return carouselSample.reply();
       default:
          Parrot parrot = new Parrot(event);
          return parrot.reply();
    }
  }

  // ------------ 中略・変更なし ------------ 
  
}
```

### 動作確認

**必ずスマートフォン（タブレット）のLINEアプリで動作確認してください（PC版でも表示されますが、ボタンが押せないことがあります）**

1. LinebotApplication を一度停止して、再起動する
2. LINEアプリで、Botに向かって `カルーセル` と送信する
3. Botが複数のバブル（カルーセル）を表示する<br>![Flex02](Flex02.png)

-----

[戻る](../../README.md)
