package com.hacheery.ecommerce.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(min = 2, max = 200, message = "Tên sản phẩm phải từ 2-200 ký tự")
    private String name;

    @Size(max = 5000, message = "Mô tả không được quá 5000 ký tự")
    private String description;

    @NotNull(message = "Giá không được để trống")
    @Min(value = 0, message = "Giá phải lớn hơn 0")
    private Double price;

    @Min(value = 0, message = "Giá giảm phải lớn hơn hoặc bằng 0")
    private Double discountPrice;

    @NotBlank(message = "SKU không được để trống")
    private String sku;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 0, message = "Số lượng phải lớn hơn hoặc bằng 0")
    private Integer stock;

    private String imageUrl;

    @NotNull(message = "Category ID không được để trống")
    private Long categoryId;
    private Long sellerId;
}
