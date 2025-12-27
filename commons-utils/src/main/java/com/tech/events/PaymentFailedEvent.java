package com.tech.events;

public record PaymentFailedEvent(String orderId,
                                 String paymentId,
                                 boolean isSuccessful,
                                 String userId,
                                 String productId,
                                 int quantity,
                                 String details) {
}
