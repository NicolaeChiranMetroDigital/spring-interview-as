package com.interview.as.model;

public enum QueueItemType {
  CUSTOMER_SYNC(false),
  INVOICE_EXPORT(true),
  PAYMENT_RECONCILIATION(false),
  ACCOUNT_CLOSURE(false);

  private final boolean shouldFailDelivery;

  QueueItemType(boolean shouldFailDelivery) {
    this.shouldFailDelivery = shouldFailDelivery;
  }

  public boolean shouldFailDelivery() {
    return shouldFailDelivery;
  }
}
