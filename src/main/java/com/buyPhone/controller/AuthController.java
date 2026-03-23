package com.buyPhone.controller;

import com.buyPhone.dto.ApiResponse;
import com.buyPhone.dto.AuthResponse;
import com.buyPhone.dto.LoginRequest;
import com.buyPhone.dto.UserDTO;
import com.buyPhone.exception.ConflictException;
import com.buyPhone.exception.UnauthorizedException;
import com.buyPhone.service.impl.UserService;
import com.buyPhone.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JWTUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    // Login Endpoint
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody LoginRequest request) {

        UserDTO user = userService.findByEmail(request.email())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        if (!passwordEncoder.matches(request.passwordHash(), user.getPasswordHash())) {
            throw new UnauthorizedException("Invalid email or password");
        }
        String token = jwtUtil.generateToken(user.getId().toString(), user.getRole());

        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Login successful")
                .data(new AuthResponse(token, user.getRole()))
                .timestamp(LocalDateTime.now())
                .build()
        );
    }

    // Register Endpoint
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@RequestBody UserDTO userDTO) {

        if (userService.existByEmail(userDTO.getEmail())) {
            throw new ConflictException("Email already registered");
        }



        UserDTO savedUser = userService.createUser(userDTO);
        // forcefully returned empty password for security reasons
        savedUser.setPasswordHash("");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.builder()
                        .success(true)
                        .message("Registration successful")
                        .data(savedUser)
                        .timestamp(LocalDateTime.now())
                        .build()
                );
    }

    // Token Refresh Endpoint
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<?>> refreshToken(@RequestHeader("Authorization") String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Missing or invalid Authorization header");
        }

        String oldToken = authHeader.substring(7);

        if (!jwtUtil.isTokenValid(oldToken)) {
            throw new UnauthorizedException("Invalid or expired token");
        }

        String userId = jwtUtil.extractUserId(oldToken);
        String role = jwtUtil.extractRole(oldToken);

        String newToken = jwtUtil.generateToken(userId, role);

        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Token refreshed successfully")
                .data(new AuthResponse(newToken, role))
                .timestamp(LocalDateTime.now())
                .build()
        );
    }

}
