package com.buyPhone.controller;


import com.buyPhone.dto.ApiResponse;
import com.buyPhone.dto.OrderDTO;
import com.buyPhone.dto.UserDTO;
import com.buyPhone.service.impl.UserService;
import com.buyPhone.service.interfac.IOrderService;
import com.buyPhone.service.interfac.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final IUserService userService;
    private final IOrderService orderService;


    @PostMapping
    public ApiResponse<UserDTO> createUser(@RequestBody UserDTO dto) {
        return ApiResponse.<UserDTO>builder()
                .success(true)
                .message("User created successfully")
                .data(userService.createUser(dto))
                .timestamp(java.time.LocalDateTime.now())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<UserDTO> getUser(@PathVariable UUID id) {
        return ApiResponse.<UserDTO>builder()
                .success(true)
                .message("User fetched successfully")
                .data(userService.getUser(id))
                .timestamp(java.time.LocalDateTime.now())
                .build();
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    @PutMapping("/{id}")
    public ApiResponse<UserDTO> updateUser(@PathVariable UUID id, @RequestBody UserDTO dto) {
        return ApiResponse.<UserDTO>builder()
                .success(true)
                .message("User updated successfully")
                .data(userService.updateUser(id, dto))
                .timestamp(java.time.LocalDateTime.now())
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ApiResponse.<Void>builder()
                .success(true)
                .message("User deleted successfully")
                .data(null)
                .timestamp(java.time.LocalDateTime.now())
                .build();
    }

    // 1. ADMIN ONLY: Get all users
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<UserDTO>> getAllUsers() {
        return ApiResponse.<List<UserDTO>>builder()
                .success(true)
                .message("All users fetched")
                .data(userService.getAllUsers())
                .timestamp(java.time.LocalDateTime.now())
                .build();
    }

    // 2. LOGGED-IN USER: Get their own order history
    @GetMapping("/my-history")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ApiResponse<List<OrderDTO>> getMyHistory() {
        // get userId from security context
        String userIdStr = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        UUID userId = UUID.fromString(userIdStr);

        return ApiResponse.<List<OrderDTO>>builder()
                .success(true)
                .message("Your history fetched")
                .data(orderService.getOrdersByUserId(userId))
                .timestamp(java.time.LocalDateTime.now())
                .build();
    }

    // 3. ADMIN ONLY: Get history of a specific user
    @GetMapping("/{id}/history")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<OrderDTO>> getUserHistory(@PathVariable UUID id) {
        return ApiResponse.<List<OrderDTO>>builder()
                .success(true)
                .message("User history fetched by admin")
                .data(orderService.getOrdersByUserId(id))
                .timestamp(java.time.LocalDateTime.now())
                .build();
    }
}
