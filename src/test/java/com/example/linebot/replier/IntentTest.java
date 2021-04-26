package com.example.linebot.replier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

public class IntentTest {

  @Test
  @DisplayName("通常の文字列にはUNKNOWNを返す")
  void UNKNOWNを返す() {
    var actual = Intent.whichIntent("こんにちは");
    assertThat(actual).isEqualTo(Intent.UNKNOWN);
//    fail();
  }

  @Test
  @DisplayName("数字2桁:数字2桁で始まる文字列にはREMINDERを返す")
  void REMINDERを返す() {
    var actual = Intent.whichIntent("13:15に授業");
    assertThat(actual).isEqualTo(Intent.REMINDER);
//    fail();
  }

}
