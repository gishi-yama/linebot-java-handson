## Flex Message を使う

Flex Message は、コンポーネントベースでBotの吹き出し（「バブル」という）を作成できる。

- [公式ドキュメント：Flex Messageを使う](https://developers.line.me/ja/docs/messaging-api/using-flex-messages/)
- [公式ドキュメント：Flex Messageの要素](https://developers.line.me/ja/docs/messaging-api/flex-message-elements/)

例えば、写真と説明文とボタンを一つのバブルにまとめて表示したり、複数のバブルを横スクロールで表示する（「カルーセル」という）こともできる。

### バブルを表示する

必要に応じて、以下を import に追加する（同名のクラスが多いので注意）。

- `com.linecorp.bot.model.message.FlexMessage`
- `com.linecorp.bot.model.message.flex.component.*`
- `com.linecorp.bot.model.message.flex.container.Bubble`
- `com.linecorp.bot.model.message.flex.unit.FlexAlign`
- `com.linecorp.bot.model.message.flex.unit.FlexFontSize`
- `com.linecorp.bot.model.message.flex.unit.FlexLayout`
- `java.util.Arrays`

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
      case "部屋":
        RoomInfo roomInfo = new RoomInfo();
        return roomInfo.reply();
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


```


#### Collback クラスを変更

必要に応じて、以下を import に追加する（同名のクラスが多いので注意）。

- `java.net.URI`
- `com.linecorp.bot.model.action.URIAction`
- `com.linecorp.bot.model.action.URIAction.AltUri`
- `com.linecorp.bot.model.message.flex.container.Carousel`

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
       case "部屋":
          RoomInfo roomInfo = new RoomInfo();
          return roomInfo.reply();
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

1. LineBotApplication を一度停止して、再起動する
2. LINEアプリで、Botに向かって `カルーセル` と送信する
3. Botが複数のバブル（カルーセル）を表示する<br>![Flex02](Flex02.png)

-----

[戻る](../README.md)
