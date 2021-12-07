package com.example.linebot.value;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.List;

public class CovidItems {

  private final List<CovidItem> itemList;

  @JsonCreator
  public CovidItems(List<CovidItem> itemList) {
    this.itemList = itemList;
  }

  public List<CovidItem> getItemList() {
    return itemList;
  }
}
