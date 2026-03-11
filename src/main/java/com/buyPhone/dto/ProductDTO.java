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
public class ProductDTO {

    private String id;

    private String name;
    private String description;

    private Double price;

    private String categoryId;

    private LocalDateTime createdAt;

    private String imageUrl;

    private InventoryDTO inventory;
}
