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
  public static Intent whichIntent(String text) {
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
    Intent intent = Intent.whichIntent(text);
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

```java
package com.example.linebot.service;

import com.example.linebot.replier.Intent;

import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReminderSlot {

  private final LocalTime pushAt;
  private final String pushText;

  public ReminderSlot(String text) {
    // Slotにあたる部分を取り出す正規表現の仕組み（Matcher）を作る
    String regexp = Intent.REMINDER.getRegexp();
    Pattern pattern = Pattern.compile(regexp);
    Matcher matcher = pattern.matcher(text);
    if (matcher.matches()) {
      // text の中で、"hh:mmに〇〇" の正規表現にマッチした 1 hh 2 mm 3 〇〇 を取り出す
      int hours = Integer.parseInt(matcher.group(1));
      int minutes = Integer.parseInt(matcher.group(2));
      this.pushAt = LocalTime.of(hours, minutes);
      this.pushText = matcher.group(3);
    } else {
      // 正規表現にマッチしない場合、実行時例外を throw する
      throw new IllegalArgumentException("text に渡された分をスロットに分けられません");
    }
  }

  public LocalTime getPushAt() {
    return pushAt;
  }

  public String getPushText() {
    return pushText;
  }
}
```

```java
package com.example.linebot.value;

public class ReminderItem {

  private final String userId;
  private final ReminderSlot slot;

  public ReminderItem(String userId, ReminderSlot slot) {
    this.userId = userId;
    this.slot = slot;
  }

  public String getUserId() {
    return userId;
  }

  public ReminderSlot getSlot() {
    return slot;
  }
}
```

```java
package com.example.linebot.replier;

import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

public class RemindOn implements Replier {

  private final String text;

  public RemindOn(String text) {
    this.text = text;
  }

  @Override
  public Message reply() {
    return new TextMessage(text + " を登録しました");
  }
}
```


```java
package com.example.linebot.service;

import com.example.linebot.replier.RemindOn;
import com.example.linebot.value.ReminderItem;
import com.example.linebot.value.ReminderSlot;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import org.springframework.stereotype.Service;

@Service
public class RemainderService {

  public RemindOn doReplyOfNewItem(MessageEvent<TextMessageContent> event) {
    String userId = event.getSource().getUserId();
    TextMessageContent tmc = event.getMessage();
    String text = tmc.getText();
    ReminderSlot slot = new ReminderSlot(text);
    ReminderItem item = new ReminderItem(userId, slot);


    return new RemindOn(text);
  }
  
}
```


```java
private final RemainderService remainderService;

@Autowired
public Callback(RemainderService remainderService) {
  this.remainderService = remainderService;
}
```