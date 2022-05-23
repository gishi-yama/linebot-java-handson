package com.example.linebot.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDate;

//スネークケース abc_de のキーと、キャメルケース abcDe のフィールド名をマッピングする
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CovidItem {

  private final LocalDate date;
  private final String nameJP;
  private final int npatients;

  @JsonCreator
  public CovidItem(LocalDate date, String nameJP, int npatients) {
    this.date = date;
    this.nameJP = nameJP;
    this.npatients = npatients;
  }

  public LocalDate getDate() {
    return date;
  }

  public String getNameJP() {
    return nameJP;
  }

  public int getNpatients() {
    return npatients;
  }
}
