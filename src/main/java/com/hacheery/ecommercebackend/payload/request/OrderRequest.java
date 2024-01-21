package com.hacheery.ecommercebackend.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private Long id;

    private Date orderDate;

    private double totalAmount;

    private Long customerId;
}
