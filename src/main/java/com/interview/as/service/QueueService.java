package com.interview.as.service;

import com.interview.as.model.CountryCode;
import com.interview.as.model.QueueItem;
import com.interview.as.model.QueueItemStatus;
import com.interview.as.model.QueueItemType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

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

  public Map<String, Object> processCountry(CountryCode countryCode) {
    List<QueueItem> pendingItems = acquirePendingItems(countryCode);
    if (pendingItems.isEmpty()) {
      return Map.of(
          "countryCode", countryCode,
          "status", "EMPTY",
          "message", "No pending items available for the selected country.");
    }

    List<Map<String, Object>> processedItems = new ArrayList<>();
    QueueItem lastItem = null;
    boolean allDelivered = true;

    //TODO: Implement the processing of the pending items, including retry logic and error handling.

    Map<String, Object> response = responseBody(lastItem, allDelivered);
    response.put("status", allDelivered ? lastItem.status() : QueueItemStatus.FAILED);
    response.put(
        "message",
        allDelivered
            ? "All items delivered to dummy API."
            : "One or more items failed while processing the country queue.");
    response.put("processedCount", processedItems.size());
    response.put("items", processedItems);
    return response;
  }

  private boolean tryDeliver(QueueItem item) {
    boolean delivered = false;
    String lastError = null;

    //TODO: Implement the retry logic for delivering the item to the dummy API, updating the item's status and last error accordingly.

    return delivered;
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

  private List<QueueItem> acquirePendingItems(CountryCode countryCode) {
    return queue.stream()
        .filter(item -> item.countryCode() == countryCode)
        .filter(QueueItem::isAvailableForProcessing)
        .sorted(Comparator.comparingLong(QueueItem::id))
        .toList();
  }

  private void seedQueue() {
    queue.add(new QueueItem(nextId.getAndIncrement(), QueueItemType.CUSTOMER_SYNC, CountryCode.RO));
    queue.add(new QueueItem(nextId.getAndIncrement(), QueueItemType.INVOICE_EXPORT, CountryCode.DE));
    queue.add(new QueueItem(nextId.getAndIncrement(), QueueItemType.PAYMENT_RECONCILIATION, CountryCode.RO));
    queue.add(new QueueItem(nextId.getAndIncrement(), QueueItemType.ACCOUNT_CLOSURE, CountryCode.FR));
    queue.add(new QueueItem(nextId.getAndIncrement(), QueueItemType.INVOICE_EXPORT, CountryCode.DE));
  }
}
