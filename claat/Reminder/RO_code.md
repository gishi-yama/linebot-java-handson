use https://codeimg.io/

```java
package com.example.linebot.replier;

public enum Intent {
}
```

```java
package com.example.linebot.replier;

import java.util.EnumSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Intent {

  // メッセージの正規表現パターンに対応するやりとり状態の定義
  REMINDER("^(\\d{1,2}):(\\d{1,2})に(.{1,32})$"),
  UNKNOWN(".+");

  private final String regexp;

  private Intent(String regexp) {
    this.regexp = regexp;
  }

  // メッセージからやりとり状態を判断
  public static Intent makeIntent(String text) {
    // 全てのIntent（REMINDER, UNKNOWN）を取得
    EnumSet<Intent> set = EnumSet.allOf(Intent.class);
    // 引数 text が、REMINDER, UNKNOWN のパターンに当てはまるかチェック
    // 当てはまった方を戻り値とする
    for (Intent intent : set) {
      if (Pattern.matches(intent.regexp, text)) {
        return intent;
      }
    }
    return UNKNOWN;
  }
}
```

```java
import com.example.linebot.replier.Intent;
```

```java
import com.example.linebot.replier.Intent;

@LineMessageHandler
public class Callback {

  // ------------ 中略・変更なし ------------

  // 文章で話しかけられたとき（テキストメッセージのイベント）に対応する
  @EventMapping
  public Message handleMessage(MessageEvent<TextMessageContent> event) {
    TextMessageContent tmc = event.getMessage();
    String text = tmc.getText();
    Intent intent = Intent.makeIntent(text);
    switch (intent) {
      case REMINDER:
        return new TextMessage("リマインダーです");
      case UNKNOWN:
        Parrot parrot = new Parrot(event);
        return parrot.reply();
    }
  }
}
```