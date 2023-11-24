package com.example.linebot.replier;

import com.example.linebot.value.CovidItem;
import com.example.linebot.value.CovidItems;
import com.linecorp.bot.messaging.model.Message;
import com.linecorp.bot.messaging.model.TextMessage;

import java.time.Month;
import java.util.List;

public class CovidReport implements Replier {

  private static final String MESSAGE_FORMAT = "%s の %s月%s日 の新規感染者数は %d 人";

  private final CovidItems items;

  public CovidReport(CovidItems items) {
    this.items = items;
  }

  @Override
  public Message reply() {
    String body = "データがありません";
    List<CovidItem> list = items.getItemList();
    if (list.size() >= 2) {
      body = getMessageOf2days();
    }
    if (list.size() == 1) {
      body = getMessageOfLast();
    }
    return new TextMessage(body);
  }

  private String getMessageOf2days() {
    List<CovidItem> list = items.getItemList();
    CovidItem before = list.get(0);
    CovidItem beforeLast = list.get(1);
    int differ = before.getNpatients() - beforeLast.getNpatients();
    Month month = before.getDate().getMonth();
    int dayOfMonth = before.getDate().getDayOfMonth();
    return String.format(MESSAGE_FORMAT, before.getNameJP(), month.getValue(), dayOfMonth, differ);
  }

  private String getMessageOfLast() {
    List<CovidItem> list = items.getItemList();
    CovidItem before = list.get(0);
    Month month = before.getDate().getMonth();
    int dayOfMonth = before.getDate().getDayOfMonth();
    return String.format(MESSAGE_FORMAT, before.getNameJP(), month.getValue(), dayOfMonth, before.getNpatients());
  }

}
