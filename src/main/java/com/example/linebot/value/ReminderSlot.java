package com.example.linebot.value;

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
      // text の中で、"hh:mmに〇〇" の正規表現に
      // マッチした 1 hh 2 mm 3 〇〇 を取り出す
      int hours = Integer.parseInt(matcher.group(1));
      int minutes = Integer.parseInt(matcher.group(2));
      this.pushAt = LocalTime.of(hours, minutes);
      this.pushText = matcher.group(3);
    } else {
      // 正規表現にマッチしない場合、実行時例外を throw する
      throw new IllegalArgumentException("text をスロットに分けられません");
    }
  }

  public LocalTime getPushAt() {
    return pushAt;
  }

  public String getPushText() {
    return pushText;
  }
}
