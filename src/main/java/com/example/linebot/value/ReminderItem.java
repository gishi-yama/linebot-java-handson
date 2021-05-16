package com.example.linebot.value;

import java.time.LocalTime;

public class ReminderItem {

  private final String userId;
  private final ReminderSlot slot;

  public ReminderItem(String userId, ReminderSlot slot) {
    this.userId = userId;
    this.slot = slot;
  }

  public ReminderItem() {
    this.userId = null;
    this.slot = null;
  }

  public ReminderItem(String userId, LocalTime push_at, String push_text) {
    this.userId = userId;
    this.slot = new ReminderSlot(push_at, push_text);
  }

  public String getUserId() {
    return userId;
  }

  public ReminderSlot getSlot() {
    return slot;
  }
}