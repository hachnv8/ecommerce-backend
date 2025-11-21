package com.hacheery.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryTreeDTO {
    private Long id;
    private String name;
    private String description;
    private String slug;
    private Boolean isActive;
    private List<CategoryTreeDTO> children;
}
