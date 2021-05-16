package com.example.linebot.value;

import java.time.LocalTime;

public class ReminderItemTuple {

  private final String userId;
  private final LocalTime pushAt;
  private final String pushText;

  public ReminderItemTuple(String userId, LocalTime pushAt, String pushText) {
    this.userId = userId;
    this.pushAt = pushAt;
    this.pushText = pushText;
  }

  public String getUserId() {
    return userId;
  }

  public LocalTime getPushAt() {
    return pushAt;
  }

  public String getPushText() {
    return pushText;
  }
}
