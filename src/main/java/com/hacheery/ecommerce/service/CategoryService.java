package com.hacheery.ecommerce.service;

import com.hacheery.ecommerce.dto.CategoryTreeDTO;
import com.hacheery.ecommerce.entity.Category;
import com.hacheery.ecommerce.exception.ResourceNotFoundException;
import com.hacheery.ecommerce.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // Lấy category theo id, nếu không có sẽ throw ResourceNotFoundException
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .filter(Category::getIsActive)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + id + " not found"));
    }

    // Lấy cây category active
    public List<Category> getCategoryTree() {
        List<Category> allCategories = categoryRepository.findAll()
                .stream()
                .filter(Category::getIsActive)
                .toList();

        Map<Long, Category> map = new HashMap<>();
        for (Category cat : allCategories) {
            map.put(cat.getId(), cat);
            cat.setChildren(new HashSet<>());
        }

        List<Category> roots = new ArrayList<>();
        for (Category cat : allCategories) {
            if (cat.getParent() != null && map.containsKey(cat.getParent().getId())) {
                map.get(cat.getParent().getId()).getChildren().add(cat);
            } else {
                roots.add(cat);
            }
        }
        return roots;
    }

    // Tạo mới category (set isActive true)
    public Category createCategory(Category category) {
        category.setIsActive(true);
        return categoryRepository.save(category);
    }

    // Cập nhật category, nếu không tìm thấy trả lỗi
    public Category updateCategory(Long id, Category updated) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + id + " not found"));
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setSlug(updated.getSlug());
        existing.setParent(updated.getParent());
        existing.setIsActive(updated.getIsActive());
        return categoryRepository.save(existing);
    }

    // Soft delete category nếu có, nếu không throw lỗi
    public void softDeleteCategory(Long id) {
        Category cat = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + id + " not found"));
        cat.setIsActive(false);
        categoryRepository.save(cat);
    }

    // Phương thức đệ quy để xây dựng cây Category DTO gây ra N+1 query
    public List<CategoryTreeDTO> getCategoryTreeRecursive() {
        // Lấy danh sách category gốc (không có parent)
        List<Category> rootCategories = categoryRepository.findByParentId(null)
                .stream().filter(Category::getIsActive)
                .toList();

        return rootCategories.stream().map(this::toCategoryTreeDTO)
                .toList();
    }

    private CategoryTreeDTO toCategoryTreeDTO(Category category) {
        List<CategoryTreeDTO> children = categoryRepository.findByParentId(category.getId())
                .stream()
                .filter(Category::getIsActive)
                .map(this::toCategoryTreeDTO)
                .toList();
        return new CategoryTreeDTO(category.getId(),
                category.getName(),
                category.getDescription(),
                category.getSlug(),
                category.getIsActive(),
                children);
    }
}
