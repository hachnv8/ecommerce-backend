package com.hacheery.ecommerce.repository;

import com.hacheery.ecommerce.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Lấy danh sách cha root (không có parent)
    @Query("SELECT c FROM Category c WHERE c.parent IS NULL")
    List<Category> findRootCategories();

    // Lấy danh sách con của một parent
    List<Category> findByParentId(Long parentId);

    // Lấy cây cây con đệ quy (có thể cần sử dụng native query hoặc recursive CTE nếu DB hỗ trợ)
}