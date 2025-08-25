package com.hacheery.ecommerce.dto;

import lombok.Data;

import java.util.Map;

@Data
public class CreateOrderRequest {
    private Long userId;
    private String customerName;
    private String shippingAddress;
    private String phoneNumber;

    // Map<bookId, quantity>
    private Map<Long, Integer> items;
}
