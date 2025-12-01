package com.hacheery.ecommerce.service;

import com.hacheery.ecommerce.dto.CategoryDTO;
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

    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .filter(Category::getIsActive)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + id + " not found"));
        return toCategoryDTO(category);
    }

    public List<CategoryTreeDTO> getCategoryTree() {
        List<Category> allCategories = categoryRepository.findAll()
                .stream()
                .filter(Category::getIsActive)
                .toList();

        Map<Long, CategoryTreeDTO> categoryMap = new HashMap<>();
        List<CategoryTreeDTO> roots = new ArrayList<>();

        for (Category cat : allCategories) {
            CategoryTreeDTO dto = new CategoryTreeDTO(
                    cat.getId(),
                    cat.getName(),
                    cat.getDescription(),
                    cat.getSlug(),
                    cat.getIsActive(),
                    new ArrayList<>()
            );
            categoryMap.put(cat.getId(), dto);
            if (cat.getParent() == null) {
                roots.add(dto);
            }
        }

        for (Category cat : allCategories) {
            if (cat.getParent() != null) {
                CategoryTreeDTO parentDTO = categoryMap.get(cat.getParent().getId());
                if (parentDTO != null) {
                    parentDTO.getChildren().add(categoryMap.get(cat.getId()));
                }
            }
        }

        return roots;
    }

    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .filter(Category::getIsActive)
                .map(this::toCategoryDTO)
                .collect(Collectors.toList());
    }

    // Tạo mới category từ DTO, trả về DTO
    public CategoryDTO createCategory(CategoryDTO dto) {
        Category category = new Category();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setSlug(dto.getSlug());
        category.setIsActive(true);

        if (dto.getParentId() != null) {
            Category parent = categoryRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent category with id " + dto.getParentId() + " not found"));
            category.setParent(parent);
        }

        Category saved = categoryRepository.save(category);
        return toCategoryDTO(saved);
    }

    // Cập nhật category từ DTO, trả về DTO
    public CategoryDTO updateCategory(Long id, CategoryDTO dto) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + id + " not found"));

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setSlug(dto.getSlug());
        existing.setIsActive(dto.getIsActive());

        if (dto.getParentId() != null) {
            Category parent = categoryRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent category with id " + dto.getParentId() + " not found"));
            existing.setParent(parent);
        } else {
            existing.setParent(null);
        }

        Category saved = categoryRepository.save(existing);
        return toCategoryDTO(saved);
    }

    public void softDeleteCategory(Long id) {
        Category cat = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + id + " not found"));
        cat.setIsActive(false);
        categoryRepository.save(cat);
    }

    // Helper chuyển entity sang DTO
    public CategoryDTO toCategoryDTO(Category category) {
        return new CategoryDTO(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getSlug(),
                category.getIsActive(),
                category.getParent() != null ? category.getParent().getId() : null,
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }
}
