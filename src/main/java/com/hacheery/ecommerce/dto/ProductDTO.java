package com.hacheery.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private String name;
    private String description;
    private Double price;
    private Double discountPrice;
    private String sku;
    private Integer stock;
    private String imageUrl;
    private Long categoryId;
    private Long sellerId;
}
