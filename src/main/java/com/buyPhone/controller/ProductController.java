package com.buyPhone.controller;

import com.buyPhone.dto.*;
import com.buyPhone.service.interfac.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final IProductService productService;

    // CREATE PRODUCT (Admin Only)
    // Uses consumes = MULTIPART_FORM_DATA_VALUE to handle image and JSON
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ProductDetailDTO> createProduct(
            @RequestPart("image") MultipartFile image,
            @RequestPart("product") ProductDetailDTO dto) {

        return ApiResponse.<ProductDetailDTO>builder()
                .success(true)
                .message("Product created successfully")
                .data(productService.createProduct(image, dto))
                .timestamp(LocalDateTime.now())
                .build();
    }

    // GET ALL PRODUCTS (Customer View - Summarized)
    @GetMapping
    public ApiResponse<Page<ProductSummaryDTO>> getAllProducts(Pageable pageable) {
        return ApiResponse.<Page<ProductSummaryDTO>>builder()
                .success(true)
                .message("Products fetched successfully")
                .data(productService.getAllProductSummary(pageable))
                .timestamp(LocalDateTime.now())
                .build();
    }

    // GET PRODUCT DETAILS (Customer View - Full Details)
    @GetMapping("/{id}")
    public ApiResponse<ProductDetailDTO> getProductDetails(@PathVariable UUID id) {
        return ApiResponse.<ProductDetailDTO>builder()
                .success(true)
                .message("Product details fetched successfully")
                .data(productService.getProductDetails(id))
                .timestamp(LocalDateTime.now())
                .build();
    }

    // SEARCH PRODUCTS
    @GetMapping("/search")
    public ApiResponse<Page<ProductDetailDTO>> searchProducts(
            @RequestParam String keyword,
            Pageable pageable) {
        return ApiResponse.<Page<ProductDetailDTO>>builder()
                .success(true)
                .message("Search results fetched")
                .data(productService.searchProducts(keyword, pageable))
                .timestamp(LocalDateTime.now())
                .build();
    }

    // GET PRODUCTS BY CATEGORY
    @GetMapping("/category/{categoryId}")
    public ApiResponse<Page<ProductDetailDTO>> getProductsByCategory(
            @PathVariable UUID categoryId,
            Pageable pageable) {
        return ApiResponse.<Page<ProductDetailDTO>>builder()
                .success(true)
                .message("Products for category fetched")
                .data(productService.getProductsByCategory(categoryId, pageable))
                .timestamp(LocalDateTime.now())
                .build();
    }

    // ADMIN VIEW (Full details including Inventory)
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Page<ProductAdminDTO>> getAllProductsForAdmin(Pageable pageable) {
        return ApiResponse.<Page<ProductAdminDTO>>builder()
                .success(true)
                .message("Admin product list fetched")
                .data(productService.getAllProductsForAdmin(pageable))
                .timestamp(LocalDateTime.now())
                .build();
    }

    // UPDATE PRODUCT
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ProductDetailDTO> updateProduct(
            @PathVariable UUID id,
            @RequestBody ProductDetailDTO dto) {
        return ApiResponse.<ProductDetailDTO>builder()
                .success(true)
                .message("Product updated successfully")
                .data(productService.updateProduct(id, dto))
                .timestamp(LocalDateTime.now())
                .build();
    }

    // UPDATE STOCK ONLY (Fast Operation)
    @PatchMapping("/{id}/stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> updateStock(
            @PathVariable UUID id,
            @RequestParam Integer quantity) {
        productService.updateStock(id, quantity);
        return ApiResponse.<Void>builder()
                .success(true)
                .message("Stock updated successfully")
                .timestamp(LocalDateTime.now())
                .build();
    }

    // DELETE PRODUCT
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ApiResponse.<Void>builder()
                .success(true)
                .message("Product deleted successfully")
                .timestamp(LocalDateTime.now())
                .build();
    }
}