package com.buyPhone.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDetailDTO {
    private UUID id;
    private String name;
    private String description;
    private Double price;
    private String imageUrl;
    private LocalDateTime createdAt;
    private Map<String, Object> attributes;

    // Use ID for input, and a nested DTO for output
    private UUID categoryId;
    private String categoryName;

    // This is used ONLY for creation input
    private Integer initialQuantity;
}
