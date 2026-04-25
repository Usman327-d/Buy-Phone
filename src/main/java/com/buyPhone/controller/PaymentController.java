package com.buyPhone.controller;


import com.buyPhone.dto.ApiResponse;
import com.buyPhone.service.interfac.IPaymentService;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments/")
public class PaymentController {

    private final IPaymentService paymentService;

    @PostMapping("/checkout/{orderId}")
    public ResponseEntity<ApiResponse<String>> checkout(@PathVariable UUID orderId) throws StripeException {
        String checkoutUrl = paymentService.processCheckout(orderId);

        return ResponseEntity.ok(ApiResponse.<String>builder()
                .success(true)
                .message("Checkout URL generated")
                .data(checkoutUrl)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        paymentService.processWebhook(payload, sigHeader);
        return ResponseEntity.ok("Success");
    }
}
