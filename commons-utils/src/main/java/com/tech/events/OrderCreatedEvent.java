package com.tech.events;

public record OrderCreatedEvent(String orderId,
                                String productId,
                                int quantity,
                                String status,
                                String userId) {
}
