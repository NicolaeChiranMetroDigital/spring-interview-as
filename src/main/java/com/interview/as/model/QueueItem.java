package com.interview.as.model;

public class QueueItem {
  private final long id;
  private final QueueItemType type;
  private final CountryCode countryCode;
  private QueueItemStatus status;
  private int attempts;
  private String lastError;

  public QueueItem(long id, QueueItemType type, CountryCode countryCode) {
    this.id = id;
    this.type = type;
    this.countryCode = countryCode;
    this.status = QueueItemStatus.PENDING;
    this.attempts = 0;
  }

  public long id() {
    return id;
  }

  public QueueItemType type() {
    return type;
  }

  public CountryCode countryCode() {
    return countryCode;
  }

  public QueueItemStatus status() {
    return status;
  }

  public int attempts() {
    return attempts;
  }

  public String lastError() {
    return lastError;
  }

  public boolean isAvailableForProcessing() {
    return status == QueueItemStatus.PENDING || status == QueueItemStatus.RETRY_PENDING;
  }

  public void incrementAttempts() {
    attempts++;
  }

  public void markRetryPending(String errorMessage) {
    status = QueueItemStatus.RETRY_PENDING;
    lastError = errorMessage;
  }

  public void markProcessed() {
    status = QueueItemStatus.PROCESSED;
    lastError = null;
  }

  public void markFailed(String errorMessage) {
    status = QueueItemStatus.FAILED;
    lastError = errorMessage;
  }
}
