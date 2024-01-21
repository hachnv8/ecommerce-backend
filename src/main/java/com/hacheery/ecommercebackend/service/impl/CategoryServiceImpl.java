package com.hacheery.ecommercebackend.service.impl;

import com.hacheery.ecommercebackend.entity.Category;
import com.hacheery.ecommercebackend.exception.DuplicateException;
import com.hacheery.ecommercebackend.exception.ResourceNotFoundException;
import com.hacheery.ecommercebackend.exception.SQLException;
import com.hacheery.ecommercebackend.payload.request.CategoryRequest;
import com.hacheery.ecommercebackend.repository.CategoryRepository;
import com.hacheery.ecommercebackend.service.CategoryService;
import com.hacheery.ecommercebackend.specification.CategorySpecification;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public Page<Category> getCategories(CategoryRequest request) {
        Pageable pageable = PageRequest.of(request.getPageNumber(), request.getPageSize());
        Specification<Category> spec = CategorySpecification.searchByParameter(
                request
        );
        return categoryRepository.findAll(spec, pageable);
    }

    @Override
    public Category getCategoryById(Long id) {
        if (categoryRepository.findById(id).isPresent()) { 
            return categoryRepository.findById(id).get();
        } else throw new ResourceNotFoundException("id", "id", id);
    }

    @Override
    public Category addCategory(Category category) {
        Objects.requireNonNull(category, "Category information must not be blank");
        if (StringUtils.isBlank(category.getName()))
            throw new IllegalArgumentException("Tên danh mục không được để trống");
        if (categoryRepository.existsByName(category.getName())) {
            throw new DuplicateException("Category name is already exist");
        }
        try {
            return categoryRepository.save(category);
        } catch (DataIntegrityViolationException e) {
            throw new SQLException("Lỗi lưu danh mục vào cơ sở dữ liệu", e);
        }
    }

    @Override
    public Category updateCategory(Long id, Category category) {
        if (categoryRepository.findById(id).isPresent()) {
            Category currentCategory = categoryRepository.findById(id).get();
            currentCategory.setDescription(category.getDescription());
            currentCategory.setName(category.getName());
            currentCategory.setShortDescription(category.getShortDescription());
            currentCategory.setParentId(category.getParentId());
            return categoryRepository.save(currentCategory);
        } else throw new ResourceNotFoundException("id", "id", id);
    }

    @Override
    public void deleteCategory(Long id) {
        if (categoryRepository.findById(id).isPresent()) {
            categoryRepository.deleteById(id);
        } else throw new ResourceNotFoundException("id", "id", id);
    }
}
