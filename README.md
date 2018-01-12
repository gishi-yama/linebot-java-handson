# 今日の内容

Java アプリケーションでLINE BOTを作ってみる

## 前準備

### ngrokをインストールする

自分のPCを使うひとは、

https://ngrok.com/download

Macの人は、端末（ターミナル）を起動して

```
brew cask install ngrok
```

### ngrokを起動する

OSの環境に合わせて、ターミナル（コマンドプロンプト）で以下を実行する

```
ngrok http 8080
```

以下の様に表示されれば成功（xxx の部分はそれぞれ異なる）

```
ngrok by @inconshreveable                                                                                                                       (Ctrl+C to quit)
                                                                                                                                                                
Session Status                online                                                                                                                            
Version                       2.2.8                                                                                                                             
Region                        United States (us)                                                                                                                
Web Interface                 http://127.0.0.1:4040                                                                                                             
Forwarding                    http://xxx.ngrok.io -> localhost:8080                                                                                        
Forwarding                    https://xxx.ngrok.io -> localhost:8080 
```

ngrokは起動したままにしておく。特に、

```
Forwarding                    https://xxx.ngrok.io -> localhost:8080
```

の部分はよく使うので、メモ帳などにコピーしておくとよい。

### srping boot のプロジェクトを作成する 

https://start.spring.io/

- **Group** は `com.example` のまま
- **Artifact**　を　`linebot` にする
- **Search for dependencies** を `web` にする

**Generate Project** ボタンを押して、zipファイルをダウンロードする

### IDEでプロジェクトを読み込む

- Netbeans
  - ファイル ＞ プロジェクトを開く でフォルダを選択
  - 読み込まれたら、プロジェクトを右クリックして「依存性でビルド」と「消去してビルド」
- Eclipse
  - ファイル ＞ インポート ＞ 既存Mavenプロジェクト でフォルダを選択
  - 読み込まれたら、プロジェクトを右クリックして Maven ＞ プロジェクトの更新
- IntelliJ Idea
  - import project でフォルダの中の pom.xml を選択
  - `import Maven projects automatically` にチェックして続行


### Spring Bootの動作確認

ソース・パッケージ（src/main/java）のcom.example.linebotパッケージの中にTestクラスを作る。


```java
package com.example.linebot;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class Test {

  @RequestMapping(method = RequestMethod.GET)
  public String hello() {
    return "Hello!";
  }

}
```

LineBotApplication を右クリックで起動する。

ブラウザから、 [http://localhost:8080/test](http://localhost:8080/test) にアクセスする。以下の様に表示されれば成功。

```
Hello!
```

ブラウザから、ngrokに表示されていた `https://xxx.ngrok.io` を使って、 `https://xxx.ngrok.io/test` にもアクセスする。

上と同じように表示されればOK。 


## LINE使う準備をする

### Message API(つまり、Bot)の登録

[LINE developers Message APIを利用するには](https://developers.line.me/ja/docs/messaging-api/getting-started/) の手順で行う。

- 途中、入力する情報（プロパイダー名、アプリ名など）は個人情報でなければ好きに入力して大丈夫。
- プランは `Developer Trial` を選ぶ。
- 大業種は `個人` 、小業種は `個人（学生）` などでよい。
- アイコン画像は適当なのがなければひぐまDukeをどうぞ。  

![ひぐまDuke](./duke_72dpi.png)

### Message APIの利用

できあがったMessage APIのチャンネルを開く。

- **アクセストークン（ロングターム）** の `再発行` ボタンを押す。（失効までの時間は0時間でよい）
- **Webhook送信**を `利用する` に変更する
- **WebHookURL**を、 `xxx.ngrok.io/callback` に変更する(xxxは各自のもの)
- **自動応答メッセージ**を `利用しない` に変更する
- **友達追加時あいさつ**を `利用しない` に変更する

QRコードが表示されているので、作ったBot友達として登録する。

### Java側の設定

リソース・パッケージ（src/main/resources）の application.properties ファイルをMessage APIの情報で書き換える。

```properties
line.bot.channel-token=アクセストークン（ロングターム）の値を改行なしで貼り付ける
line.bot.channel-secret=Channel Secretの値を改行なしで貼り付ける
handler.path=/callback
```


## Botの中身を作成する

Botに話しかけられた際の反応を行うCallbackクラスをプログラミングする。

```java
package com.example.linebot;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

@LineMessageHandler
public class Callback {

  @EventMapping
  public TextMessage handleText(MessageEvent<TextMessageContent> event) {
    System.out.println("event: " + event);
    TextMessageContent tmc = event.getMessage();
    String text = tmc.getText();
    switch (text) {
      case "こんにちは":
      default:
        return parrot(text);
    }
  }

  @EventMapping
  public void handleEvent(Event event) {
    System.out.println("event: " + event);
  }
  
  // オウム返しをする
  private TextMessage parrot(String text) { 
    return new TextMessage(text); 
  }

}
```

LineBotApplication を一度停止して、再起動する。

LINEで、botに向かって　こんにちは　と入力して、botがオウム返しをすることを確認する。

## 時間帯にあわせてあいさつするBotにする

Botのあいさつを、朝はおはよう、昼はこんにちは、夜はこんばんはと返すようにする。

Callbackクラスを改良する。

```java
package com.example.linebot;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.time.LocalTime;

@LineMessageHandler
public class Callback {

  @EventMapping
  public TextMessage handleText(MessageEvent<TextMessageContent> event) {
    System.out.println("event: " + event);
    TextMessageContent tmc = event.getMessage();
    String text = tmc.getText();
    switch (text) {
      case "こんにちは":
        return makeGreeting();
      default:
        return parrot(text);
    }
  }

  @EventMapping
  public void handleEvent(Event event) {
    System.out.println("event: " + event);
  }

  // オウム返しをする
  private TextMessage parrot(String text) {
    return new TextMessage(text);
  }

  // あいさつする
  private TextMessage makeGreeting() {
    LocalTime lt = LocalTime.now();
    int hour = lt.getHour();
    if (hour >= 6 && hour <= 11) {
      return new TextMessage("おはようございます、Dukeです");
    }
    if (hour >= 12 && hour <= 16) {
      return new TextMessage("こんにちは、Dukeです");
    }

    return new TextMessage("こんばんは、Dukeです");
  }


}
```

LineBotApplication を一度停止して、再起動する。

LINEで、botに向かって　こんにちは　と入力して、botが時間帯にあわせた返答をすることを確認する。


## センサーの情報を返答するbotにする

Botを使って、二酸化炭素センサーの値を教えてもらうようにする。

センサーの情報を受け取るCO2クラスを作成する。

```java
package com.example.linebot;

public class CO2 {

  // 二酸化炭素濃度
  private float concentration;
  // 温度
  private float temperature;

  public float getConcentration() {
    return concentration;
  }

  public void setConcentration(float concentration) {
    this.concentration = concentration;
  }

  public float getTemperature() {
    return temperature;
  }

  public void setTemperature(float temperature) {
    this.temperature = temperature;
  }

}
```

Callbackクラスを改良する。

```java
package com.example.linebot;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalTime;

@LineMessageHandler
public class Callback {

  @EventMapping
  public TextMessage handleText(MessageEvent<TextMessageContent> event) {
    System.out.println("event: " + event);
    TextMessageContent tmc = event.getMessage();
    String text = tmc.getText();
    switch (text) {
      case "こんにちは":
        return makeGreeting();
      case "教室":
          return makeRoomInfo();
      default:
        return parrot(text);
    }
  }

  @EventMapping
  public void handleEvent(Event event) {
    System.out.println("event: " + event);
  }

  // オウム返しをする
  private TextMessage parrot(String text) {
    return new TextMessage(text);
  }

  // あいさつする
  private TextMessage makeGreeting() {
    LocalTime lt = LocalTime.now();
    int hour = lt.getHour();
    if (hour >= 6 && hour <= 11) {
      return new TextMessage("おはようございます、Dukeです");
    }
    if (hour >= 12 && hour <= 16) {
      return new TextMessage("こんにちは、Dukeです");
    }

    return new TextMessage("こんばんは、Dukeです");
  }

  // センサーの値をWebから取得して、CO2クラスのインスタンスにいれる(xxxxの所は、別途指示します）
  private TextMessage makeRoomInfo() {
    String key = "xxxx";
    String url = "https://us.wio.seeed.io/v1/node/GroveCo2MhZ16UART0/concentration_and_temperature?access_token=";
    URI uri = URI.create(url + key);
    RestTemplate restTemplate = new RestTemplateBuilder().build();
    try {
      CO2 co2 = restTemplate.getForObject(uri, CO2.class);
      String message = "二酸化炭素は" + co2.getConcentration() + "ppm、温度は" + co2.getTemperature() + "度です";
      return new TextMessage(message);
    } catch (HttpClientErrorException e) {
      e.printStackTrace();
      return new TextMessage("センサーに接続できていません");
    }
  }

}
```

LineBotApplication を一度停止して、再起動する。

LINEで、botに向かって `教室` と入力して、botが二酸化炭素濃度と温度を返答することを確認する。

（keyやurlが間違っていると，センサーに接続できていませんと返答する）


## Botのいろいろな反応を作ってみよう

自分のプログラミング知識をつかって、いろんな反応を作ってみよう。

たとえば...おみくじをひく、じゃんけんする、計算する...などなど。