## LIFFを動かす

LIFFの詳細は[公式のドキュメント:LINE Front-end Framework](https://developers.line.me/ja/docs/messaging-api/using-rich-menus/)を参照されたい。

LIFFは、別途に用意したWebページやフォームをLINE Bot内部に表示できるようにする。これにより、LINEクライアントが用意するUIだけでは実現できない複雑な入力やWebアプリとの連携ができるようになる。

ここでは、

1. ライブラリにThymeleafを追加する
2. Spring BootでThymeleafのWebページを表示する
3. LIFFのサンプルを表示する

の手順で進める。

### 1. ライブラリにThymeleafを追加する

プロジェクトファイル（ルートフォルダ）の pom.xml の `<properties>〜</properties>` の中に、Thymeleafの記述を追加する。

```xml
<properties>
  (中略）
  <thymeleaf.version>3.0.9.RELEASE</thymeleaf.version>
  <thymeleaf-layout-dialect.version>2.3.0</thymeleaf-layout-dialect.version>
</properties>
```

プロジェクトファイル（ルートフォルダ）の pom.xml の `<dependencies>〜</dependencies>` の中に、Thymeleafの記述を追加する。（spring-boot-starter-webの下あたり）

```xml
<dependencies>
  （中略）
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
  </dependency>
  （中略）
</dependencies>
```

### 2. Spring BootでThymeleafのWebページを表示する

その他のソース（src/main/resources）の application.properties の末尾に、Thymeleafの設定を追加する

```properties
## thymeleaf
spring.thymeleaf.mode=HTML
```

その他のソース（src/main/resources）の templates フォルダの中に liff.html を作成する<br>（フォルダがない場合は作成する）

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
  <meta charset="UTF-8" />
  <title>Hello Thymeleaf</title>
</head>
<body>
<h1>[[${test}]]</h1>
</body>
</html>
```

ソース・パッケージ（src/main/java）に com.example.linebot.web パッケージを作成し、その中に LIFFController クラスを作成する
 
```java
package com.example.linebot.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LIFFController {

  @GetMapping("/liff")
  public String hello(Model model) {
    // [[${test}]] の部分を Hello... で書き換えて、liff.htmlを表示する
    model.addAttribute("test", "Hello by Tymeleaf!");
    return "liff";
  }

}
```

#### Thymeleafの動作確認

1. LineBotApplication を一度停止して、再起動する
2. [http://localhost:m8080/liff](http://localhost:8080/liff) にアクセスする
3. ブラウザに下のように表示されることを確認する<br>![Hello by Thymeleaf](Liff_P1_01.jpg)<br>これは、 liff.html の`[[${test}]]`の部分を、 LIFFController で書き換えている。


### 3. LIFFのサンプルを表示する

[line/line-liff-starter](https://github.com/line/line-liff-starter) のサンプルコードを（少し変更して）動作させる。

上記のサイトの liff-starter.js と style.css をコピーして、その他のソース（src/main/resources）の static フォルダの中に複製する<br>（フォルダがない場合は作成する）

上記のサイトの index.html の内容をもとに、その他のソース（src/main/resources）の templates/liff.html を書き換える。

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<!-- The html based on https://github.com/line/line-liff-starter/blob/master/index.html -->
<head>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>LIFF Starter</title>
  <link rel="stylesheet" href="style.css">
</head>

<body>

<h1>[[${test}]]</h1>

<div class="buttongroup">
  <div class="buttonrow">
    <button id="openwindowbutton">Open Window</button>
    <button id="closewindowbutton">Close Window</button>
  </div>
  <div class="buttonrow">
    <button id="getprofilebutton">Get Profile</button>
    <button id="sendmessagebutton">Send Message</button>
  </div>
</div>

<div id="profileinfo">
  <h2>Profile</h2>
  <a href="#" onclick="toggleProfileData()">Close Profile</a>
  <div id="profilepicturediv">
  </div>
  <table border="1">
    <tr>
      <th>userId</th>
      <td id="useridprofilefield"></td>
    </tr>
    <tr>
      <th>displayName</th>
      <td id="displaynamefield"></td>
    </tr>
    <tr>
      <th>statusMessage</th>
      <td id="statusmessagefield"></td>
    </tr>
  </table>
</div>

<div id="liffdata">
  <h2>LIFF Data</h2>
  <table border="1">
    <tr>
      <th>language</th>
      <td id="languagefield"></td>
    </tr>
    <tr>
      <th>context.viewType</th>
      <td id="viewtypefield"></td>
    </tr>
    <tr>
      <th>context.userId</th>
      <td id="useridfield"></td>
    </tr>
    <tr>
      <th>context.utouId</th>
      <td id="utouidfield"></td>
    </tr>
    <tr>
      <th>context.roomId</th>
      <td id="roomidfield"></td>
    </tr>
    <tr>
      <th>context.groupId</th>
      <td id="groupidfield"></td>
    </tr>
  </table>
</div>

<script src="https://d.line-scdn.net/liff/1.0/sdk.js"></script>
<script src="liff-starter.js"></script>
</body>
</html>
```



-----

[戻る](../../README.md)
