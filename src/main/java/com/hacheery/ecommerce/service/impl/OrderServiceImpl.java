package com.hacheery.ecommerce.service.impl;

import com.hacheery.ecommerce.dto.CreateOrderRequest;
import com.hacheery.ecommerce.entity.*;
import com.hacheery.ecommerce.event.OrderCreatedEvent;
import com.hacheery.ecommerce.kafka.OrderProducer;
import com.hacheery.ecommerce.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements com.hacheery.ecommerce.service.OrderService {

    private final OrderRepository orderRepository;
    private final OrderProducer orderProducer;

    @Override
    public Order createOrder(CreateOrderRequest request) {
        // Tạo order
        Order order = Order.builder()
                .userId(request.getUserId())
                .customerName(request.getCustomerName())
                .shippingAddress(request.getShippingAddress())
                .phoneNumber(request.getPhoneNumber())
                .status(OrderStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .build();

        // Thêm order items
        List<OrderItem> items = new ArrayList<>();
        request.getItems().forEach((bookId, qty) -> {
            OrderItem item = OrderItem.builder()
                    .bookId(bookId)
                    .quantity(qty)
                    .price(0) // TODO: sau này lấy từ BookService
                    .orderId(order.getId())
                    .build();
            items.add(item);
        });
        order.setItemIdList(items.stream().map(OrderItem::getId).collect(Collectors.toList()));

        // Lưu DB
        Order savedOrder = orderRepository.save(order);

        // Gửi event Kafka
        OrderCreatedEvent event = new OrderCreatedEvent(savedOrder.getId(), savedOrder.getUserId(), savedOrder.getStatus().name());
        orderProducer.sendOrderCreatedEvent(event);

        return savedOrder;
    }
}
