package com.buyPhone.controller;

import com.buyPhone.dto.ApiResponse;
import com.buyPhone.dto.OrderDTO;
import com.buyPhone.service.impl.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping("/place/{userId}")
    public ApiResponse<OrderDTO> placeOrder(@PathVariable UUID userId) {
        return ApiResponse.<OrderDTO>builder()
                .success(true)
                .message("Order placed successfully")
                .data(service.placeOrder(userId))
                .timestamp(java.time.LocalDateTime.now())
                .build();
    }

    @GetMapping("/{orderId}")
    public ApiResponse<OrderDTO> getOrder(@PathVariable UUID orderId) {
        return ApiResponse.<OrderDTO>builder()
                .success(true)
                .message("Order fetched successfully")
                .data(service.getOrder(orderId))
                .timestamp(java.time.LocalDateTime.now())
                .build();
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<OrderDTO>> getUserOrders(@PathVariable UUID userId) {
        return ApiResponse.<List<OrderDTO>>builder()
                .success(true)
                .message("User orders fetched successfully")
                .data(service.getUserOrders(userId))
                .timestamp(java.time.LocalDateTime.now())
                .build();
    }

    @PutMapping("/{orderId}/status")
    public ApiResponse<OrderDTO> updateOrderStatus(@PathVariable UUID orderId, @RequestParam String status) {
        return ApiResponse.<OrderDTO>builder()
                .success(true)
                .message("Order status updated successfully")
                .data(service.updateOrderStatus(orderId, status))
                .timestamp(java.time.LocalDateTime.now())
                .build();
    }
}