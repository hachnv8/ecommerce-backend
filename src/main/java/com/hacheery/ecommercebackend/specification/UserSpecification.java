package com.hacheery.ecommercebackend.specification;


import com.hacheery.ecommercebackend.payload.request.UserRequest;
import com.hacheery.ecommercebackend.security.entity.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
    public static Specification<User> searchByParameter(UserRequest userRequest) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if(shouldIncludeUsername(userRequest.getName())) {
                predicate = cb.and(predicate, cb.like(root.get("name"), "%"
                + userRequest.getName() + "%"));
            }
            return predicate;
        };
    }

    private static boolean shouldIncludeUsername(String username) {
        return username != null && !username.isEmpty();
    }
}
