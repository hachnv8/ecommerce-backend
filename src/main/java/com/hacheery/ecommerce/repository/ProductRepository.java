package com.hacheery.ecommerce.repository;

import com.hacheery.ecommerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // Tìm theo SKU
    Optional<Product> findBySku(String sku);

    boolean existsBySku(String sku);

    // Tìm theo category
    List<Product> findByCategoryId(Long categoryId);

    List<Product> findByCategoryIdAndIsActive(Long categoryId, Boolean isActive);

    // Tìm theo seller
    List<Product> findBySeller_Id(Long sellerId);

    List<Product> findBySeller_IdAndIsActive(Long sellerId, Boolean isActive);

    // Tìm theo trạng thái
    List<Product> findByIsActive(Boolean isActive);

    // Tìm theo tên (search)
    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByNameContainingIgnoreCaseAndIsActive(String name, Boolean isActive);

    // Tìm theo khoảng giá
    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice AND p.isActive = true")
    List<Product> findByPriceRange(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);

    // Tìm sản phẩm đang sale (có discount)
    @Query("SELECT p FROM Product p WHERE p.discountPrice IS NOT NULL AND p.discountPrice < p.price AND p.isActive = true")
    List<Product> findProductsOnSale();

    // Tìm sản phẩm sắp hết hàng
    @Query("SELECT p FROM Product p WHERE p.stock <= :threshold AND p.isActive = true")
    List<Product> findLowStockProducts(@Param("threshold") Integer threshold);

    // Tìm sản phẩm theo category và price range
    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId " +
            "AND p.price BETWEEN :minPrice AND :maxPrice " +
            "AND p.isActive = true")
    List<Product> findByCategoryAndPriceRange(
            @Param("categoryId") Long categoryId,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice
    );

    // Pagination - sản phẩm mới nhất
    Page<Product> findByIsActiveOrderByCreatedAtDesc(Boolean isActive, Pageable pageable);

    // Pagination - sản phẩm theo category
    Page<Product> findByCategoryIdAndIsActive(Long categoryId, Boolean isActive, Pageable pageable);

    // Count products by category
    @Query("SELECT COUNT(p) FROM Product p WHERE p.category.id = :categoryId AND p.isActive = true")
    Long countByCategoryId(@Param("categoryId") Long categoryId);

    // Count products by seller
    @Query("SELECT COUNT(p) FROM Product p WHERE p.seller.id = :sellerId")
    Long countBySellerId(@Param("sellerId") Long sellerId);
}
