package com.example.linebot.repository;

import com.example.linebot.value.ReminderItem;
import com.example.linebot.value.ReminderSlot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ReminderRepository {

  private final JdbcTemplate jdbc;

  @Autowired
  public ReminderRepository(JdbcTemplate jdbcTemplate) {
    this.jdbc = jdbcTemplate;
  }

  public void insert(ReminderItem item) {
    // language=sql
    String sql = "insert into reminder_item " +
      "(user_id, push_at, push_text) " +
      "values (?, ?, ?)";

    String userId = item.getUserId();
    ReminderSlot slot = item.getSlot();
    jdbc.update(sql, userId, slot.getPushAt(), slot.getPushText());
  }

}
