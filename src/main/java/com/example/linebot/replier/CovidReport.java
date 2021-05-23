package com.example.linebot.replier;

import com.example.linebot.value.CovidItem;
import com.example.linebot.value.CovidItems;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

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
    String body = getLastCovidInfomation();
    return new TextMessage(body);
  }

  private String getLastCovidInfomation() {
    List<CovidItem> list = items.getItemList();
    if (list.size() >= 2) {
      // 政府のデータ取得2日目以降
      CovidItem before = list.get(0);
      CovidItem beforeLast = list.get(1);
      int differ = before.getNpatients() - beforeLast.getNpatients();
      Month month = before.getDate().getMonth();
      int dayOfMonth = before.getDate().getDayOfMonth();
      return String.format(MESSAGE_FORMAT, before.getNameJP(), month.getValue(), dayOfMonth, differ);
    }
    if (list.size() == 1) {
      // 政府のデータ取得初日
      CovidItem before = list.get(0);
      Month month = before.getDate().getMonth();
      int dayOfMonth = before.getDate().getDayOfMonth();
      return String.format(MESSAGE_FORMAT, before.getNameJP(), month.getValue(), dayOfMonth, before.getNpatients());
    }
    return "データがありません";
  }

}
