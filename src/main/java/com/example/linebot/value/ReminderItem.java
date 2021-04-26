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