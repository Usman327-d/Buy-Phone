package com.buyPhone.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductSummaryDTO {

    private UUID id;
    private String name;
    private Double price;
    private String imageUrl;
}
