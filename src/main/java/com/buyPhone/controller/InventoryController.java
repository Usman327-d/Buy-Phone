package com.buyPhone.controller;

import com.buyPhone.dto.ApiResponse;
import com.buyPhone.service.impl.InventoryService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    @PostMapping("/decrease/{productId}")
    public ApiResponse<Void> decreaseStock(@PathVariable UUID productId, @RequestParam int quantity) {
        service.decreaseStock(productId, quantity);
        return ApiResponse.<Void>builder()
                .success(true)
                .message("Stock decreased successfully")
                .data(null)
                .timestamp(java.time.LocalDateTime.now())
                .build();
    }

    @PostMapping("/increase/{productId}")
    public ApiResponse<Void> increaseStock(@PathVariable UUID productId, @RequestParam int quantity) {
        service.increaseStock(productId, quantity);
        return ApiResponse.<Void>builder()
                .success(true)
                .message("Stock increased successfully")
                .data(null)
                .timestamp(java.time.LocalDateTime.now())
                .build();
    }
}
