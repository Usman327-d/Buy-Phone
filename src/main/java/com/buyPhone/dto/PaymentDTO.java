package com.buyPhone.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;



@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {

    private String id;

    private String paymentMethod;

    private String paymentStatus;

    private String transactionId;

    private LocalDateTime paidAt;
}
