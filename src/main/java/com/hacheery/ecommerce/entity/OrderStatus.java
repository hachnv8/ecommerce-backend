package com.hacheery.ecommerce.entity;

public enum OrderStatus {
    CREATED,
    INVENTORY_RESERVED,
    INVENTORY_FAILED,
    PAYMENT_SUCCESS,
    PAYMENT_FAILED,
    SHIPPED,
    CANCELLED
}
