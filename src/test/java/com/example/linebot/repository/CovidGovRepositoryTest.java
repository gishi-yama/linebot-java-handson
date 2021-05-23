package com.example.linebot.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CovidGovRepositoryTest {

  CovidGovRepository sut;

  @Autowired
  public CovidGovRepositoryTest(CovidGovRepository sut) {
    this.sut = sut;
  }

  @Test
  void 指定した県のCovidItemを取得できる() {
    var returning = sut.findCovidGovAPI("北海道");
    var actual = returning.getItemList().get(0);
    assertNotNull(actual);
//    fail();
  }
}
