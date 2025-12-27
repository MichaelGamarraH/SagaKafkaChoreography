package com.tech.model;

public record Transaction(String transactionId, String orderId,
                          boolean isSuccessful, String userId) {
}
