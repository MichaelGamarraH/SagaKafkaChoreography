package com.tech.events;

public record PaymentCompletedEvent(
        String orderId,
        String paymentId,
        boolean isSuccessful,
        String userId
) { }
