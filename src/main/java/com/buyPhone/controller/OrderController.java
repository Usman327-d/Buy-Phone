package com.buyPhone.controller;

import com.buyPhone.dto.ApiResponse;
import com.buyPhone.dto.OrderDTO;
import com.buyPhone.dto.OrderRequestDTO;
import com.buyPhone.dto.UserDTO;
import com.buyPhone.service.interfac.IOrderService;
import com.buyPhone.service.interfac.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;
    private final IUserService userService;



    @PostMapping("/place-order")
    public ResponseEntity<ApiResponse<OrderDTO>> placeOrder(@RequestBody OrderRequestDTO request) {
        // 1. Extract the userId from the Security Context
        // Note: This assumes your JWT Filter sets the userId as the 'name' or principal
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID userId = UUID.fromString(authentication.getName());

        // 2. Call the Service with the extracted ID and the body
        OrderDTO orderDTO = orderService.placeOrder(request, userId);

        // 3. Build the standardized response
        ApiResponse<OrderDTO> response = ApiResponse.<OrderDTO>builder()
                .success(true)
                .message("Order placed successfully")
                .data(orderDTO)
                .timestamp(java.time.LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{orderId}")
    public ApiResponse<OrderDTO> getOrder(@PathVariable UUID orderId) {
        return ApiResponse.<OrderDTO>builder()
                .success(true)
                .message("Order fetched successfully")
                .data(orderService.getOrder(orderId))
                .timestamp(java.time.LocalDateTime.now())
                .build();
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public ApiResponse<List<OrderDTO>> getUserOrders(@PathVariable UUID userId) {
        return ApiResponse.<List<OrderDTO>>builder()
                .success(true)
                .message("User orders fetched successfully")
                .data(orderService.getUserOrders(userId))
                .timestamp(java.time.LocalDateTime.now())
                .build();
    }


    @PutMapping("/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<OrderDTO> updateOrderStatus(@PathVariable UUID orderId, @RequestParam(name = "status") String status) {
        return ApiResponse.<OrderDTO>builder()
                .success(true)
                .message("Order status updated successfully")
                .data(orderService.updateOrderStatus(orderId, status))
                .timestamp(java.time.LocalDateTime.now())
                .build();
    }




}