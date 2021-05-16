package com.example.linebot.repository;

import com.example.linebot.value.ReminderItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
public class ReminderRepositoryTest {

  ReminderRepository sut;

  @Autowired
  public ReminderRepositoryTest(ReminderRepository sut) {
    this.sut = sut;
  }

  @Test
  void test() {
    List<ReminderItem> previousItem = sut.findPreviousItem();
    assertEquals(0, previousItem.size());
  }

}
