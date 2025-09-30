package com.coincraft.models;

import java.time.LocalDateTime;

/**
 * Immutable record of a SmartCoin transaction for auditing and history.
 */
public class TransactionRecord {
    public enum Type { CREDIT, DEBIT, TRANSFER }

    private final String transactionId;
    private final Type type;
    private final String fromUserId;
    private final String toUserId;
    private final int amount;
    private final String reason;
    private final LocalDateTime timestamp;

    public TransactionRecord(String transactionId,
                             Type type,
                             String fromUserId,
                             String toUserId,
                             int amount,
                             String reason,
                             LocalDateTime timestamp) {
        this.transactionId = transactionId;
        this.type = type;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.amount = amount;
        this.reason = reason;
        this.timestamp = timestamp != null ? timestamp : LocalDateTime.now();
    }

    public String getTransactionId() { return transactionId; }
    public Type getType() { return type; }
    public String getFromUserId() { return fromUserId; }
    public String getToUserId() { return toUserId; }
    public int getAmount() { return amount; }
    public String getReason() { return reason; }
    public LocalDateTime getTimestamp() { return timestamp; }
}


