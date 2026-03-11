package com.buyPhone.controller;


import com.buyPhone.dto.ApiResponse;
import com.buyPhone.dto.UserDTO;
import com.buyPhone.service.impl.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public ApiResponse<UserDTO> createUser(@RequestBody UserDTO dto) {
        return ApiResponse.<UserDTO>builder()
                .success(true)
                .message("User created successfully")
                .data(service.createUser(dto))
                .timestamp(java.time.LocalDateTime.now())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<UserDTO> getUser(@PathVariable UUID id) {
        return ApiResponse.<UserDTO>builder()
                .success(true)
                .message("User fetched successfully")
                .data(service.getUser(id))
                .timestamp(java.time.LocalDateTime.now())
                .build();
    }

    @GetMapping
    public ApiResponse<List<UserDTO>> getAllUsers() {
        return ApiResponse.<List<UserDTO>>builder()
                .success(true)
                .message("Users fetched successfully")
                .data(service.getAllUsers())
                .timestamp(java.time.LocalDateTime.now())
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<UserDTO> updateUser(@PathVariable UUID id, @RequestBody UserDTO dto) {
        return ApiResponse.<UserDTO>builder()
                .success(true)
                .message("User updated successfully")
                .data(service.updateUser(id, dto))
                .timestamp(java.time.LocalDateTime.now())
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable UUID id) {
        service.deleteUser(id);
        return ApiResponse.<Void>builder()
                .success(true)
                .message("User deleted successfully")
                .data(null)
                .timestamp(java.time.LocalDateTime.now())
                .build();
    }
}
