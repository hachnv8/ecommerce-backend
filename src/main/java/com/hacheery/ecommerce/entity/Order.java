package com.hacheery.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;  // để đơn giản, chưa liên kết sang UserService

    private String customerName;

    private String shippingAddress;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private LocalDateTime createdAt;

    private String listItemId;

    @Transient
    public List<Long> getItemIdList() {
        if (listItemId == null || listItemId.isEmpty()) return List.of();
        return Arrays.stream(listItemId.split(","))
                .map(Long::parseLong)
                .toList();
    }

    @Transient
    public void setItemIdList(List<Long> itemIdList) {
        this.listItemId = itemIdList.stream().map(String::valueOf).collect(Collectors.joining(","));
    }
}
