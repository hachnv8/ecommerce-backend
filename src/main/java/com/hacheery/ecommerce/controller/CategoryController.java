package com.hacheery.ecommerce.controller;

import com.hacheery.ecommerce.dto.BaseResponseDTO;
import com.hacheery.ecommerce.dto.CategoryDTO;
import com.hacheery.ecommerce.dto.CategoryTreeDTO;
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

    // GET /api/categories - danh sách phẳng CategoryDTO (public)
    @GetMapping
    public ResponseEntity<BaseResponseDTO<List<CategoryDTO>>> getAllCategories() {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        BaseResponseDTO<List<CategoryDTO>> response = new BaseResponseDTO<>(
                HttpStatus.OK.value(),
                "Success",
                categories,
                LocalDateTime.now(),
                null
        );
        return ResponseEntity.ok(response);
    }

    // GET /api/categories/{id} - 1 CategoryDTO (public)
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponseDTO<CategoryDTO>> getCategoryById(@PathVariable Long id) {
        CategoryDTO category = categoryService.getCategoryById(id);
        BaseResponseDTO<CategoryDTO> response = new BaseResponseDTO<>(
                HttpStatus.OK.value(),
                "Category found",
                category,
                LocalDateTime.now(),
                null
        );
        return ResponseEntity.ok(response);
    }

    // GET /api/categories/tree - cây CategoryTreeDTO (public)
    @GetMapping("/tree")
    public ResponseEntity<BaseResponseDTO<List<CategoryTreeDTO>>> getCategoryTree() {
        List<CategoryTreeDTO> tree = categoryService.getCategoryTree();
        BaseResponseDTO<List<CategoryTreeDTO>> response = new BaseResponseDTO<>(
                HttpStatus.OK.value(),
                "Category tree retrieved successfully",
                tree,
                LocalDateTime.now(),
                null
        );
        return ResponseEntity.ok(response);
    }

    // POST /api/categories - tạo Category, trả về CategoryDTO (ADMIN)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponseDTO<CategoryDTO>> createCategory(@RequestBody CategoryDTO categoryDTO) {
        CategoryDTO createdDTO = categoryService.createCategory(categoryDTO);
        BaseResponseDTO<CategoryDTO> response = new BaseResponseDTO<>(
                HttpStatus.CREATED.value(),
                "Category created",
                createdDTO,
                LocalDateTime.now(),
                null
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // PUT /api/categories/{id} - cập nhật Category, trả về CategoryDTO (ADMIN)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponseDTO<CategoryDTO>> updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryDTO categoryDTO
    ) {
        CategoryDTO updatedDTO = categoryService.updateCategory(id, categoryDTO);
        BaseResponseDTO<CategoryDTO> response = new BaseResponseDTO<>(
                HttpStatus.OK.value(),
                "Category updated",
                updatedDTO,
                LocalDateTime.now(),
                null
        );
        return ResponseEntity.ok(response);
    }

    // DELETE /api/categories/{id} - soft delete (ADMIN)
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