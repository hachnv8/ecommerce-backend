package com.hacheery.ecommerce.service;

import com.hacheery.ecommerce.dto.CreateOrderRequest;
import com.hacheery.ecommerce.entity.Order;

public interface OrderService {
    Order createOrder(CreateOrderRequest request);
}
