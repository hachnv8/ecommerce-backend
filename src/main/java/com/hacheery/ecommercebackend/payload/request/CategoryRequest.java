package com.hacheery.ecommercebackend.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest extends PaginationRequest {
    private String categoryName;
    private Long parentId;
}
