package com.example.linebot.replier;

import com.example.linebot.value.CovidItem;
import com.example.linebot.value.CovidItems;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

import java.util.List;
import java.util.stream.Collectors;

public class CovidReport implements Replier {

  private static final String MESSAGE_FORMAT = "%s の %s の新規感染者数は %d 人";

  private final CovidItems items;

  public CovidReport(CovidItems items) {
    this.items = items;
  }

  @Override
  public Message reply() {
    List<CovidItem> twoItems = cutOff();
    String body = getLastCovidInfomation(twoItems);
    return new TextMessage(body);
  }

  private String getLastCovidInfomation(List<CovidItem> covidItems) {
    switch (covidItems.size()) {
      case 2:
        // データ取得2日目以降
        CovidItem before = covidItems.get(0);
        CovidItem beforeLast = covidItems.get(1);
        int differ = before.getNpatients() - beforeLast.getNpatients();
        return String.format(MESSAGE_FORMAT, before.getNameJP(), before.getDate(), differ);
      case 1:
        // データ取得初日
        CovidItem one = covidItems.get(0);
        return String.format(MESSAGE_FORMAT, one.getNameJP(), one.getDate(), one.getNpatients());
      default:
        return "データが不足しています";
    }
  }

  // 不要なデータを切り捨てる
  private List<CovidItem> cutOff() {
    return items.getItemList().stream()
      .limit(2)
      .collect(Collectors.toList());
  }
}
