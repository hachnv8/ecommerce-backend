package com.hacheery.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "categories", indexes = {
        @Index(name = "idx_category_slug", columnList = "slug"),
        @Index(name = "idx_category_name", columnList = "name"),
        @Index(name = "idx_category_active", columnList = "is_active"),
        @Index(name = "idx_category_parent", columnList = "parent_id"),
        @Index(name = "idx_category_parent_active", columnList = "parent_id, is_active")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private Set<Category> children;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(nullable = false)
    private Boolean isActive;
}
