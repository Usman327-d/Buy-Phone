package com.buyPhone.controller;

import com.buyPhone.dto.ApiResponse;
import com.buyPhone.dto.CategoryDTO;
import com.buyPhone.service.impl.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService service;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<CategoryDTO> createCategory(@RequestBody CategoryDTO dto) {
        return ApiResponse.<CategoryDTO>builder()
                .success(true)
                .message("Category created successfully")
                .data(service.createCategory(dto))
                .timestamp(java.time.LocalDateTime.now())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CategoryDTO> getCategory(@PathVariable UUID id) {
        return ApiResponse.<CategoryDTO>builder()
                .success(true)
                .message("Category fetched successfully")
                .data(service.getCategory(id))
                .timestamp(java.time.LocalDateTime.now())
                .build();
    }

    @GetMapping
    public ApiResponse<List<CategoryDTO>> getAllCategories() {
        return ApiResponse.<List<CategoryDTO>>builder()
                .success(true)
                .message("Categories fetched successfully")
                .data(service.getAllCategories())
                .timestamp(java.time.LocalDateTime.now())
                .build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<CategoryDTO> updateCategory(@PathVariable UUID id, @RequestBody CategoryDTO dto) {
        return ApiResponse.<CategoryDTO>builder()
                .success(true)
                .message("Category updated successfully")
                .data(service.updateCategory(id, dto))
                .timestamp(java.time.LocalDateTime.now())
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteCategory(@PathVariable UUID id) {
        service.deleteCategory(id);
        return ApiResponse.<Void>builder()
                .success(true)
                .message("Category deleted successfully")
                .data(null)
                .timestamp(java.time.LocalDateTime.now())
                .build();
    }

    @GetMapping("/distinct-category-names")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<String>> getCategoryNames() {
        return ApiResponse.<List<String>>builder()
                .success(true)
                .message("Category deleted successfully")
                .data(service.getDistinctCategoryNames())
                .timestamp(java.time.LocalDateTime.now())
                .build();
    }
}
