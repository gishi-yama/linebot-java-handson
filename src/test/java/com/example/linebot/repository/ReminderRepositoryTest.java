package com.example.linebot.repository;

import com.example.linebot.value.ReminderItemTuple;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ReminderRepositoryTest {

  ReminderRepository sut;

  @Autowired
  public ReminderRepositoryTest(ReminderRepository sut) {
    this.sut = sut;
  }

  @Test
  void test() {
    List<ReminderItemTuple> previousItems = sut.findPreviousItems();
    ReminderItemTuple reminderItemTuple = previousItems.get(0);
    System.out.println(reminderItemTuple);
    assertEquals(0, previousItems.size());
  }


}
