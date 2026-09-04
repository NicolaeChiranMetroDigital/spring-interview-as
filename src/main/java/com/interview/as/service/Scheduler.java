package com.interview.as.service;

import com.interview.as.model.CountryCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Scheduler {
  private static final Logger LOGGER = LoggerFactory.getLogger(Scheduler.class);

  private final QueueService queueService;

  public Scheduler(QueueService queueService) {
    this.queueService = queueService;
  }

  //TODO: Implement a scheduled task that runs once a day at a different hour for each country (e.g., FR).
  // The scheduled task should log the result of the processing.
}
