package com.hacheery.ecommerce.security.service;

import com.hacheery.ecommerce.dto.ProductDTO;
import com.hacheery.ecommerce.entity.Category;
import com.hacheery.ecommerce.entity.Product;
import com.hacheery.ecommerce.exception.BadRequestException;
import com.hacheery.ecommerce.exception.DuplicateResourceException;
import com.hacheery.ecommerce.exception.ResourceNotFoundException;
import com.hacheery.ecommerce.repository.CategoryRepository;
import com.hacheery.ecommerce.repository.ProductRepository;
import com.hacheery.ecommerce.security.entity.User;
import com.hacheery.ecommerce.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    // Create product
    public Product createProduct(ProductDTO productDTO, Long sellerId) {
        // Validate category exists
        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy category với id: " + productDTO.getCategoryId()));

        // Validate seller exists
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user với id: " + sellerId));

        // Validate unique SKU
        if (productRepository.existsBySku(productDTO.getSku())) {
            throw new DuplicateResourceException("SKU '" + productDTO.getSku() + "' đã tồn tại");
        }

        // Validate price
        if (productDTO.getPrice() <= 0) {
            throw new BadRequestException("Giá sản phẩm phải lớn hơn 0");
        }

        // Validate discount price
        if (productDTO.getDiscountPrice() != null && productDTO.getDiscountPrice() >= productDTO.getPrice()) {
            throw new BadRequestException("Giá giảm phải nhỏ hơn giá gốc");
        }

        // Create product
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setDiscountPrice(productDTO.getDiscountPrice());
        product.setSku(productDTO.getSku());
        product.setStock(productDTO.getStock());
        product.setImageUrl(productDTO.getImageUrl());
        product.setCategory(category);
        product.setSeller(seller);
        product.setIsActive(true);

        return productRepository.save(product);
    }

    // Update product
    public Product updateProduct(Long productId, ProductDTO productDTO, Long currentUserId, String currentUserRole) {
        // Find product
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm với id: " + productId));

        // Authorization: seller can only edit own products
        if ("SELLER".equals(currentUserRole) && !product.getSeller().getId().equals(currentUserId)) {
            throw new BadRequestException("Bạn không có quyền chỉnh sửa sản phẩm này");
        }

        // Validate category exists
        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy category với id: " + productDTO.getCategoryId()));

        // Validate unique SKU (if changed)
        if (!product.getSku().equals(productDTO.getSku()) && productRepository.existsBySku(productDTO.getSku())) {
            throw new DuplicateResourceException("SKU '" + productDTO.getSku() + "' đã tồn tại");
        }

        // Validate price
        if (productDTO.getPrice() <= 0) {
            throw new BadRequestException("Giá sản phẩm phải lớn hơn 0");
        }

        // Validate discount price
        if (productDTO.getDiscountPrice() != null && productDTO.getDiscountPrice() >= productDTO.getPrice()) {
            throw new BadRequestException("Giá giảm phải nhỏ hơn giá gốc");
        }

        // Update product
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setDiscountPrice(productDTO.getDiscountPrice());
        product.setSku(productDTO.getSku());
        product.setStock(productDTO.getStock());
        product.setImageUrl(productDTO.getImageUrl());
        product.setCategory(category);

        return productRepository.save(product);
    }

    // Soft delete
    public void softDeleteProduct(Long productId, Long currentUserId, String currentUserRole) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm với id: " + productId));

        // Authorization: seller can only delete own products
        if ("SELLER".equals(currentUserRole) && !product.getSeller().getId().equals(currentUserId)) {
            throw new BadRequestException("Bạn không có quyền xóa sản phẩm này");
        }

        product.setIsActive(false);
        productRepository.save(product);
    }

    // Hard delete (ADMIN only)
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm với id: " + productId));

        productRepository.delete(product);
    }

    // Get by ID
    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm với id: " + productId));
    }

    // Get all products
    public List<Product> getAllProducts(Boolean isActive) {
        if (isActive != null) {
            return productRepository.findByIsActive(isActive);
        }
        return productRepository.findAll();
    }

    // Get products by seller
    public List<Product> getProductsBySeller(Long sellerId) {
        return productRepository.findBySeller_Id(sellerId);
    }

    // Get products by category
    public List<Product> getProductsByCategory(Long categoryId, Boolean isActive) {
        if (isActive != null) {
            return productRepository.findByCategoryIdAndIsActive(categoryId, isActive);
        }
        return productRepository.findByCategoryId(categoryId);
    }
}
