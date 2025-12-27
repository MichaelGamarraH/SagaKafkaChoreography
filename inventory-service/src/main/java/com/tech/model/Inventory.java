package com.tech.model;

public record Inventory(String productId,
                        String productName,
                        int quantityInStock,
                        int price) {
}