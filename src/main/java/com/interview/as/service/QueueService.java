package com.interview.as.service;

import com.interview.as.model.CountryCode;
import com.interview.as.model.QueueItem;
import com.interview.as.model.QueueItemType;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class QueueService {
  private static final Logger LOGGER = LoggerFactory.getLogger(QueueService.class);
  private static final int MAX_RETRIES = 3;

  private final AtomicLong nextId = new AtomicLong(1);
  private final DummyApiClient dummyApiClient;
  private final List<QueueItem> queue = new ArrayList<>();

  public QueueService(DummyApiClient dummyApiClient) {
    this.dummyApiClient = dummyApiClient;
    seedQueue();
  }

  public Map<String, Object> processNext(CountryCode countryCode) {
    Optional<QueueItem> next = acquireNextPendingItem(countryCode);
    if (next.isEmpty()) {
      return Map.of(
          "countryCode", countryCode,
          "status", "EMPTY",
          "message", "No pending items available for the selected country.");
    }

    QueueItem item = next.get();
    boolean delivered = false;
    String lastError = null;

    //TODO Implement the retry logic for processing the queue item.
    // The item should be retried up to MAX_RETRIES times in case of failure.
    // If the item is successfully delivered, mark it as processed.
    // If it fails after all retries, mark it as failed and store the last error message.

    return responseBody(item, delivered);
  }

  private Map<String, Object> responseBody(QueueItem item, boolean delivered) {
    Map<String, Object> response = new LinkedHashMap<>();
    response.put("id", item.id());
    response.put("type", item.type());
    response.put("countryCode", item.countryCode());
    response.put("status", item.status());
    response.put("attempts", item.attempts());
    response.put("finishedWithError", !delivered);
    response.put("message", delivered ? "Item delivered to dummy API." : item.lastError());
    return response;
  }

  private Optional<QueueItem> acquireNextPendingItem(CountryCode countryCode) {
    return queue.stream()
        .filter(item -> item.countryCode() == countryCode)
        .filter(QueueItem::isAvailableForProcessing)
        .min(Comparator.comparingLong(QueueItem::id));
  }

  private void seedQueue() {
    queue.add(new QueueItem(nextId.getAndIncrement(), QueueItemType.CUSTOMER_SYNC, CountryCode.RO));
    queue.add(new QueueItem(nextId.getAndIncrement(), QueueItemType.INVOICE_EXPORT, CountryCode.DE));
    queue.add(new QueueItem(nextId.getAndIncrement(), QueueItemType.PAYMENT_RECONCILIATION, CountryCode.RO));
    queue.add(new QueueItem(nextId.getAndIncrement(), QueueItemType.ACCOUNT_CLOSURE, CountryCode.FR));
    queue.add(new QueueItem(nextId.getAndIncrement(), QueueItemType.INVOICE_EXPORT, CountryCode.DE));
  }
}
