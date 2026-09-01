package com.interview.as.service;

import com.interview.as.model.QueueItem;
import org.springframework.stereotype.Component;

@Component
public class DummyApiClient {
  public void send(QueueItem item) {
    if (item.type().shouldFailDelivery()) {
      throw new IllegalStateException(
          "Dummy API rejected item type " + item.type() + " for country " + item.countryCode());
    }
  }
}
