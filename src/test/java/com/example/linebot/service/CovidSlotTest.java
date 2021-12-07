package com.example.linebot.service;

import com.example.linebot.value.CovidSlot;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class CovidSlotTest {

  @Test
  void 北海道のコロナ情報から北海道を抜き出す() {
    CovidSlot slot = new CovidSlot("北海道のコロナ情報");
    assertEquals("北海道", slot.getRegion());
  }

  @Test
  void 宮城県のコロナ情報から宮城県を抜き出す() {
    CovidSlot slot = new CovidSlot("宮城県のコロナ情報");
    assertEquals("宮城県", slot.getRegion());
  }
}
