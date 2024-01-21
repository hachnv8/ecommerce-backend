package com.hacheery.ecommercebackend.specification;

import com.hacheery.ecommercebackend.entity.Category;
import com.hacheery.ecommercebackend.payload.request.CategoryRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class CategorySpecification {
    public static Specification<Category> searchByParameter(CategoryRequest categoryRequest) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if(shouldIncludeCategoryName(categoryRequest.getCategoryName())) {
                predicate = cb.and(predicate, cb.like(root.get("categoryName"), "%"
                + categoryRequest.getCategoryName() + "%"));
            }
            if(shouldIncludeParentId(categoryRequest.getParentId())) {
                predicate = cb.and(predicate, cb.equal(root.get("parentId"), categoryRequest.getParentId()));
            }

            return predicate;
        };
    }

    private static boolean shouldIncludeCategoryName(String catName) {
        return catName != null && !catName.isEmpty();
    }

    private static boolean shouldIncludeParentId(Long parentId) {
        return parentId != null;
    }
}
