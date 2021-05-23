package com.example.linebot.service;

import com.example.linebot.replier.CovidReport;
import com.example.linebot.replier.PythonGreet;
import com.example.linebot.repository.CovidGovRepository;
import com.example.linebot.repository.PythonRepository;
import com.example.linebot.value.CovidItems;
import com.example.linebot.value.CovidSlot;
import com.example.linebot.value.HelloPython;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExternalService {

  private PythonRepository pythonRepository;
  private CovidGovRepository covidGovRepository;

  @Autowired
  public ExternalService(PythonRepository repository, CovidGovRepository covidGovRepository) {
    this.pythonRepository = repository;
    this.covidGovRepository = covidGovRepository;
  }

  public PythonGreet doReplyWithPython() {
    HelloPython helloPython = pythonRepository.findPythonAPI();
    return new PythonGreet(helloPython);
  }

  public CovidReport doReplyWithCovid(String text) {
    CovidSlot covidSlot = new CovidSlot(text);
    CovidItems covidItems = covidGovRepository.findCovidGovAPI(covidSlot.getRegion());
    return new CovidReport(covidItems);
  }

}
