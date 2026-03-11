package com.buyPhone.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class OrderDTO {

    private String id;

    private String status;

    private Double totalAmount;

    private LocalDateTime createdAt;

    private String userId;

    private AddressDTO shippingAddress;

    private List<OrderItemDTO> orderItems;

    private PaymentDTO payment;
}
