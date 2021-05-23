package com.example.linebot.repository;

import com.example.linebot.value.CovidItems;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class CovidGovRepository {

  private final RestTemplate restTemplate;

  @Autowired
  public CovidGovRepository(RestTemplateBuilder templateBuilder) {
    this.restTemplate = templateBuilder.build();
  }

  public CovidItems findCovidGovAPI(String region) {
    String url = String.format("https://opendata.corona.go.jp/api/Covid19JapanAll?dataName=%s", region);
    CovidItems covidItems = restTemplate.getForObject(url, CovidItems.class);
    return covidItems;
  }

}
