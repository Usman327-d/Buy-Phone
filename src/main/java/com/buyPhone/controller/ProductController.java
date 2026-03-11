package com.buyPhone.controller;

import com.buyPhone.dto.ApiResponse;
import com.buyPhone.dto.ProductDTO;
import com.buyPhone.service.impl.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping
    public ApiResponse<ProductDTO> createProduct(@RequestBody ProductDTO dto) {
        return ApiResponse.<ProductDTO>builder()
                .success(true)
                .message("Product created successfully")
                .data(service.createProduct(dto))
                .timestamp(java.time.LocalDateTime.now())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductDTO> getProduct(@PathVariable UUID id) {
        return ApiResponse.<ProductDTO>builder()
                .success(true)
                .message("Product fetched successfully")
                .data(service.getProduct(id))
                .timestamp(java.time.LocalDateTime.now())
                .build();
    }

    @GetMapping("/category/{categoryId}")
    public ApiResponse<List<ProductDTO>> getProductsByCategory(@PathVariable UUID categoryId) {
        return ApiResponse.<List<ProductDTO>>builder()
                .success(true)
                .message("Products fetched by category")
                .data(service.getProductsByCategory(categoryId))
                .timestamp(java.time.LocalDateTime.now())
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<List<ProductDTO>> searchProducts(@RequestParam String keyword) {
        return ApiResponse.<List<ProductDTO>>builder()
                .success(true)
                .message("Products search results")
                .data(service.searchProducts(keyword))
                .timestamp(java.time.LocalDateTime.now())
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductDTO> updateProduct(@PathVariable UUID id, @RequestBody ProductDTO dto) {
        return ApiResponse.<ProductDTO>builder()
                .success(true)
                .message("Product updated successfully")
                .data(service.updateProduct(id, dto))
                .timestamp(java.time.LocalDateTime.now())
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteProduct(@PathVariable UUID id) {
        service.deleteProduct(id);
        return ApiResponse.<Void>builder()
                .success(true)
                .message("Product deleted successfully")
                .data(null)
                .timestamp(java.time.LocalDateTime.now())
                .build();
    }
}
