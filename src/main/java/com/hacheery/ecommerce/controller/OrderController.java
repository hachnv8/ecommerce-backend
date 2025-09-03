package com.hacheery.ecommerce.controller;

import com.hacheery.ecommerce.dto.CreateOrderRequest;
import com.hacheery.ecommerce.entity.Order;
import com.hacheery.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderRequest request) {
        Order order = orderService.createOrder(request);
        System.out.println(order);
        return ResponseEntity.ok(order);
    }
}
