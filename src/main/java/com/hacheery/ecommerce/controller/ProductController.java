package com.hacheery.ecommerce.controller;

import com.hacheery.ecommerce.dto.BaseResponseDTO;
import com.hacheery.ecommerce.dto.ProductDTO;
import com.hacheery.ecommerce.entity.Product;
import com.hacheery.ecommerce.security.entity.User;
import com.hacheery.ecommerce.security.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    // CREATE - SELLER and ADMIN only
    @PostMapping
    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
    public ResponseEntity<BaseResponseDTO<Product>> createProduct(
            @Valid @RequestBody ProductDTO productDTO,
            @AuthenticationPrincipal UserDetails userDetails) {

        User currentUser = (User) userDetails;
        Product product = productService.createProduct(productDTO, currentUser.getId());

        BaseResponseDTO<Product> response = new BaseResponseDTO<>(
                HttpStatus.CREATED.value(),
                "Tạo sản phẩm thành công",
                product,
                LocalDateTime.now(),
                null
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // UPDATE - SELLER and ADMIN only (seller can only edit own products)
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
    public ResponseEntity<BaseResponseDTO<Product>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductDTO productDTO,
            @AuthenticationPrincipal UserDetails userDetails) {

        User currentUser = (User) userDetails;
        String role = currentUser.getAuthorities().iterator().next().getAuthority();

        Product product = productService.updateProduct(id, productDTO, currentUser.getId(), role);

        BaseResponseDTO<Product> response = new BaseResponseDTO<>(
                HttpStatus.OK.value(),
                "Cập nhật sản phẩm thành công",
                product,
                LocalDateTime.now(),
                null
        );

        return ResponseEntity.ok(response);
    }

    // SOFT DELETE - SELLER and ADMIN only
    @DeleteMapping("/{id}/soft")
    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
    public ResponseEntity<BaseResponseDTO<Void>> softDeleteProduct(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        User currentUser = (User) userDetails;
        String role = currentUser.getAuthorities().iterator().next().getAuthority();

        productService.softDeleteProduct(id, currentUser.getId(), role);

        BaseResponseDTO<Void> response = new BaseResponseDTO<>(
                HttpStatus.OK.value(),
                "Xóa sản phẩm thành công",
                null,
                LocalDateTime.now(),
                null
        );

        return ResponseEntity.ok(response);
    }

    // HARD DELETE - ADMIN only
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponseDTO<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);

        BaseResponseDTO<Void> response = new BaseResponseDTO<>(
                HttpStatus.OK.value(),
                "Xóa vĩnh viễn sản phẩm thành công",
                null,
                LocalDateTime.now(),
                null
        );

        return ResponseEntity.ok(response);
    }

    // GET by ID - Public
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponseDTO<Product>> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);

        BaseResponseDTO<Product> response = new BaseResponseDTO<>(
                HttpStatus.OK.value(),
                "Lấy thông tin sản phẩm thành công",
                product,
                LocalDateTime.now(),
                null
        );

        return ResponseEntity.ok(response);
    }

    // GET all products - Public
    @GetMapping
    public ResponseEntity<BaseResponseDTO<List<Product>>> getAllProducts(
            @RequestParam(required = false) Boolean isActive) {

        List<Product> products = productService.getAllProducts(isActive);

        BaseResponseDTO<List<Product>> response = new BaseResponseDTO<>(
                HttpStatus.OK.value(),
                "Lấy danh sách sản phẩm thành công",
                products,
                LocalDateTime.now(),
                null
        );

        return ResponseEntity.ok(response);
    }

    // GET products by seller
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<BaseResponseDTO<List<Product>>> getProductsBySeller(@PathVariable Long sellerId) {
        List<Product> products = productService.getProductsBySeller(sellerId);

        BaseResponseDTO<List<Product>> response = new BaseResponseDTO<>(
                HttpStatus.OK.value(),
                "Lấy danh sách sản phẩm của seller thành công",
                products,
                LocalDateTime.now(),
                null
        );

        return ResponseEntity.ok(response);
    }

    // GET products by category
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<BaseResponseDTO<List<Product>>> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(required = false) Boolean isActive) {

        List<Product> products = productService.getProductsByCategory(categoryId, isActive);

        BaseResponseDTO<List<Product>> response = new BaseResponseDTO<>(
                HttpStatus.OK.value(),
                "Lấy danh sách sản phẩm theo category thành công",
                products,
                LocalDateTime.now(),
                null
        );

        return ResponseEntity.ok(response);
    }

    // GET my products (current seller)
    @GetMapping("/my-products")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<BaseResponseDTO<List<Product>>> getMyProducts(
            @AuthenticationPrincipal UserDetails userDetails) {

        User currentUser = (User) userDetails;
        List<Product> products = productService.getProductsBySeller(currentUser.getId());

        BaseResponseDTO<List<Product>> response = new BaseResponseDTO<>(
                HttpStatus.OK.value(),
                "Lấy danh sách sản phẩm của bạn thành công",
                products,
                LocalDateTime.now(),
                null
        );

        return ResponseEntity.ok(response);
    }
}
