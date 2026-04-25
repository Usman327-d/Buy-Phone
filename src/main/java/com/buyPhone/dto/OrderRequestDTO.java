package com.buyPhone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderRequestDTO {
    // address details
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;

    private Boolean isDefault;

    // oder items
    private List<OrderItemDTO> orderItems;
}
