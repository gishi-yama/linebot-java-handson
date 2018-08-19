## LIFFを動かす

LIFFの詳細は[公式のドキュメント:LINE Front-end Framework](https://developers.line.me/ja/docs/messaging-api/using-rich-menus/)を参照されたい。

LIFFは、別途に用意したWebページやフォームをLINE Bot内部に表示できるようにする。これにより、LINEクライアントが用意するUIだけでは実現できない複雑な入力やWebアプリとの連携ができるようになる。

ここでは、

1. ライブラリにThymeleafを追加する
1. Spring BootでThymeleafのWebページを表示できるようにする
2. LIFFのサンプルを表示する

の手順で進める。

### ライブラリにThymeleafを追加する

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

その他のソース（src/main/resources）に templates フォルダを作成し、その中に liff.html を作成する

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

その他のソース（src/main/resources）の application.properties の末尾に、Thymeleafの設定を追加する

```properties
## thymeleaf
spring.thymeleaf.mode=HTML
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

### 動作確認

1. LineBotApplication を一度停止して、再起動する
2. [http://localhost:8080/liff](http://localhost:8080/liff) にアクセスする
3. ブラウザに下のように表示されることを確認する<br>![Hello by Thymeleaf](Liff_P1_01.jpg)<br>これは、 liff.html の`[[${test}]]`の部分を、 LIFFController で書き換えている。

-----

[戻る](../../README.md)
