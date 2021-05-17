package com.example.linebot.repository;

import com.example.linebot.value.ReminderItem;
import com.example.linebot.value.ReminderItemTuple;
import com.example.linebot.value.ReminderSlot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

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

  public List<ReminderItemTuple> findPreviousItems() {
    //language=sql
    String sql = "select user_id, push_at, push_text " +
      "from reminder_item " +
      "where push_at <= ? ";

    LocalTime now = LocalTime.now();
    List<ReminderItemTuple> list = jdbc.query(sql, new DataClassRowMapper<>(ReminderItemTuple.class), now);
    return list;
  }

  public void deletePreviousItems() {
    //language=sql
    String sql = "delete from reminder_item " +
      "where push_at <= ? ";

    LocalTime now = LocalTime.now();
    jdbc.update(sql, now);
  }


}
