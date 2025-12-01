package com.hacheery.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    private Long id;
    private String name;
    private String description;
    private String slug;
    private Boolean isActive;
    private Long parentId; // Chỉ lưu ID thay vì reference entity
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
