package com.hacheery.ecommercebackend.specification;

import com.hacheery.ecommercebackend.entity.Product;
import com.hacheery.ecommercebackend.payload.request.ProductRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

/**
 * Created by HachNV on 10/07/2023
 */
public class ProductSpecification {
    public static Specification<Product> searchByParameter(ProductRequest productRequest) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if(shouldIncludeProductName(productRequest.getName())) {
                predicate = cb.and(predicate, cb.like(root.get("name"), "%"
                + productRequest.getName() + "%"));
            }
            return predicate;
        };
    }

    private static boolean shouldIncludeProductName(String prodName) {
        return prodName != null && !prodName.isEmpty();
    }
}
