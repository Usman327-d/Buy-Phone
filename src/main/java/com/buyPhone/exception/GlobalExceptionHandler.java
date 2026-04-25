package com.buyPhone.exception;


import com.buyPhone.dto.ApiResponse;
import com.buyPhone.dto.ErrorResponse;
import com.stripe.exception.StripeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ErrorResponse buildError(
            HttpStatus status,
            String message,
            HttpServletRequest request
    ) {

        return new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI()
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request
    ) {
        log.error(String.valueOf(ex.getMessage()));
        log.error(Arrays.toString(ex.getStackTrace()));

        return new ResponseEntity<>(
                buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(
            BadRequestException ex,
            HttpServletRequest request
    ) {
        log.error(String.valueOf(ex.getMessage()));
        log.error(Arrays.toString(ex.getStackTrace()));

        return new ResponseEntity<>(
                buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(
            ConflictException ex,
            HttpServletRequest request
    ) {
        log.error(String.valueOf(ex.getMessage()));
        log.error(Arrays.toString(ex.getStackTrace()));

        return new ResponseEntity<>(
                buildError(HttpStatus.CONFLICT, ex.getMessage(), request),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(
            UnauthorizedException ex,
            HttpServletRequest request
    ) {
        log.error(String.valueOf(ex.getMessage()));
        log.error(Arrays.toString(ex.getStackTrace()));

        return new ResponseEntity<>(
                buildError(HttpStatus.UNAUTHORIZED, ex.getMessage(), request),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbidden(
            ForbiddenException ex,
            HttpServletRequest request
    ) {
        log.error(String.valueOf(ex.getMessage()));
        log.error(Arrays.toString(ex.getStackTrace()));

        return new ResponseEntity<>(
                buildError(HttpStatus.FORBIDDEN, ex.getMessage(), request),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(InventoryException.class)
    public ResponseEntity<ErrorResponse> handleInventory(
            InventoryException ex,
            HttpServletRequest request
    ) {
        log.error(String.valueOf(ex.getMessage()));
        log.error(Arrays.toString(ex.getStackTrace()));

        return new ResponseEntity<>(
                buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request),
                HttpStatus.BAD_REQUEST
        );
    }


    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            org.springframework.web.bind.MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .findFirst()
                .orElse("Validation error");

        log.error(String.valueOf(ex.getMessage()));
        log.error(Arrays.toString(ex.getStackTrace()));

        return new ResponseEntity<>(
                buildError(HttpStatus.BAD_REQUEST, message, request),
                HttpStatus.BAD_REQUEST
        );
    }


    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDatabaseError(
            Exception ex,
            HttpServletRequest request
    ) {
        log.error(String.valueOf(ex.getMessage()));
        log.error(Arrays.toString(ex.getStackTrace()));

        return new ResponseEntity<>(
                buildError(HttpStatus.CONFLICT, "Database constraint violation", request),
                HttpStatus.CONFLICT
        );
    }


    // handler to handle cloudinary cloud exception
    @ExceptionHandler(CloudServiceException.class)
    public ResponseEntity<ErrorResponse> handleCloudError(
            CloudServiceException ex,
            HttpServletRequest request) { // Removed 'String message'

        // Use ex.getMessage() to populate your error response if needed
        log.error("Cloud Service Error: {}", ex.getMessage());

        return new ResponseEntity<>(
                buildError(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage(), request),
                HttpStatus.SERVICE_UNAVAILABLE
        );
    }

    // Access Denied Exception handler
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(AccessDeniedException ex) {
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(false)
                .message("You do not have permission to perform this action.")
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN); // 403 Forbidden
    }

    // Stripe Exception
    @ExceptionHandler(StripeException.class)
    public ResponseEntity<ErrorResponse> handleStripeException(StripeException e,
                                                               HttpServletRequest request) {
        String message = "Unable to reach the payment gateway.";

        // Check if the error is specifically a network/timeout issue
        if (e.getCause() instanceof java.io.IOException) {
            message = "Network connection issue detected. Please check your internet and try again.";

            return new ResponseEntity<>(
                    buildError(HttpStatus.SERVICE_UNAVAILABLE,
                            message,
                            request),
                    HttpStatus.SERVICE_UNAVAILABLE // 503
            );
        }

        return new ResponseEntity<>(
                buildError(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Payment Error",
                            request),
                HttpStatus.INTERNAL_SERVER_ERROR // 500
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(
            Exception ex,
            HttpServletRequest request
    ) {

        log.error(String.valueOf(ex.getMessage()));
        log.error(Arrays.toString(ex.getStackTrace()));

        return new ResponseEntity<>(
                buildError(HttpStatus.INTERNAL_SERVER_ERROR,
                        "An unexpected error occurred",
                        request),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }



}
