package com.hacheery.ecommercebackend.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Created by HachNV on 10/07/2023
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest extends PaginationRequest {
    private String name;

    private String description;

    private BigDecimal price;

    private String imageUrl;

    private Long categoryId;
}
