package com.tech.model;


public record Order(String orderId,
                    String productId,
                    int quantity,
                    String status,
                    String userId) {
}



