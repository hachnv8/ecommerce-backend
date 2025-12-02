package com.hacheery.ecommerce.entity;

import com.hacheery.ecommerce.security.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "products", indexes = {
        @Index(name = "idx_product_sku", columnList = "sku"),
        @Index(name = "idx_product_category", columnList = "category_id"),
        @Index(name = "idx_product_seller", columnList = "seller_id"),
        @Index(name = "idx_product_active", columnList = "is_active"),
        @Index(name = "idx_product_name", columnList = "name"),
        @Index(name = "idx_product_category_active", columnList = "category_id, is_active")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(name = "discount_price")
    private Double discountPrice;

    @Column(nullable = false, unique = true)
    private String sku;

    @Column(nullable = false)
    private Integer stock = 0;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    // Many-to-One relationship with Category
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    // Many-to-One relationship with User (seller)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    // Constructors, getters, setters tự động có từ @Data
}
