package com.hacheery.ecommercebackend.controller;


import com.hacheery.ecommercebackend.entity.Category;
import com.hacheery.ecommercebackend.payload.request.CategoryRequest;
import com.hacheery.ecommercebackend.service.impl.CategoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryServiceImpl categoryService;

    @GetMapping
    public ResponseEntity<Page<Category>> getCategories(CategoryRequest categoryRequest) {

        Page<Category> categories = categoryService.getCategories(categoryRequest);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("{categoryId}")
    public ResponseEntity<Category> getCategoryById(@PathVariable("categoryId") Long id) {
        Category category = categoryService.getCategoryById(id);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Category> addCategory(@RequestBody Category category) {
        Category savedCategory = categoryService.addCategory(category);
        return new ResponseEntity<>(savedCategory, HttpStatus.OK);
    }

    @PutMapping("{categoryId}")
    public ResponseEntity<Category> updateCategory(@PathVariable("categoryId") Long id, @RequestBody Category category) {
        Category updatedCategory = categoryService.updateCategory(id, category);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    @DeleteMapping("{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable("categoryId") Long id) {
        categoryService.deleteCategory(id);
        return new ResponseEntity<>("Delete category successfully", HttpStatus.OK);
    }
}
