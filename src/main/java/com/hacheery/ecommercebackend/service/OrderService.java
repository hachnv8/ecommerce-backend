package com.hacheery.ecommercebackend.service;

import com.hacheery.ecommercebackend.entity.Order;
import com.hacheery.ecommercebackend.payload.request.OrderRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    Page<Order> getOrders(OrderRequest orderRequest, Pageable pageable);
    Order createOrder(Order order);
    Order getOrderById(Long orderId);
    void deleteOrder(Long orderId);
}
