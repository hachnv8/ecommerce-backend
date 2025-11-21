package com.hacheery.ecommerce.controller;

import com.hacheery.ecommerce.dto.BaseResponseDTO;
import com.hacheery.ecommerce.entity.Category;
import com.hacheery.ecommerce.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CategoryController {

    private final CategoryService categoryService;

    // Lấy toàn bộ cây category (public)
    @GetMapping
    public ResponseEntity<BaseResponseDTO<List<Category>>> getAllCategories() {
        List<Category> categories = categoryService.getCategoryTree();
        BaseResponseDTO<List<Category>> response = new BaseResponseDTO<>(
                HttpStatus.OK.value(),
                "Success",
                categories,
                LocalDateTime.now(),
                null
        );
        return ResponseEntity.ok(response);
    }

    // Lấy category theo id (public)
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponseDTO<Category>> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        BaseResponseDTO<Category> response = new BaseResponseDTO<>(
                HttpStatus.OK.value(),
                "Category found",
                category,
                LocalDateTime.now(),
                null
        );
        return ResponseEntity.ok(response);
    }

    // Tạo mới category (ADMIN)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponseDTO<Category>> createCategory(@RequestBody Category category) {
        Category created = categoryService.createCategory(category);
        BaseResponseDTO<Category> response = new BaseResponseDTO<>(
                HttpStatus.CREATED.value(),
                "Category created",
                created,
                LocalDateTime.now(),
                null
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Cập nhật category (ADMIN)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponseDTO<Category>> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        Category updated = categoryService.updateCategory(id, category);
        BaseResponseDTO<Category> response = new BaseResponseDTO<>(
                HttpStatus.OK.value(),
                "Category updated",
                updated,
                LocalDateTime.now(),
                null
        );
        return ResponseEntity.ok(response);
    }

    // Soft delete category (ADMIN)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponseDTO<Void>> softDeleteCategory(@PathVariable Long id) {
        categoryService.softDeleteCategory(id);
        BaseResponseDTO<Void> response = new BaseResponseDTO<>(
                HttpStatus.NO_CONTENT.value(),
                "Category soft deleted",
                null,
                LocalDateTime.now(),
                null
        );
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}

