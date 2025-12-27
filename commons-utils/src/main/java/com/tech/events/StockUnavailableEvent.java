package com.tech.events;

public record StockUnavailableEvent(String productId,
                                    boolean inStock,
                                    String orderId) {
}