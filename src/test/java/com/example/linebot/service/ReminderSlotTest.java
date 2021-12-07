package com.example.linebot.service;

import com.example.linebot.value.ReminderSlot;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

public class ReminderSlotTest {

  @Test
  @DisplayName("12:15に昼食を送るとReminderSlotに分解する")
  void valid1(){
    ReminderSlot sut = new ReminderSlot("12:15に昼食");
    assertThat(sut.getPushAt()).isEqualTo(LocalTime.of(12, 15));
    assertThat(sut.getPushText()).isEqualTo("昼食");
//    fail();
  }

  @Test
  @DisplayName("9:5に朝食を送るとReminderSlotに分解する")
  void valid2(){
    ReminderSlot sut = new ReminderSlot("9:5に朝食");
    assertThat(sut.getPushAt()).isEqualTo(LocalTime.of(9, 5));
    assertThat(sut.getPushText()).isEqualTo("朝食");
//    fail();
  }

  @Test
  @DisplayName("Slotにパースできない文字列を渡すとREを発生する")
  void inValid()  {
    assertThatThrownBy(() -> new ReminderSlot("お腹が減ったよ"))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage("text をスロットに分けられません");
//    fail();
  }

}
