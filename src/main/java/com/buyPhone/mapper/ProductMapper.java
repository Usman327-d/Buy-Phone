package com.buyPhone.mapper;

import com.buyPhone.dto.ProductDTO;
import com.buyPhone.entity.Product;

public class ProductMapper {

    public ProductDTO toDTO(Product product){
        if(product == null) return null;

        return ProductDTO.builder()
                .id(product.getId() != null ? product.getId().toString() : null)
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .categoryId(product.getCategory() != null ? product.getCategory().getId().toString() : null)
                .createdAt(product.getCreatedAt())
                .build();
    }

    public Product toEntity(ProductDTO dto){
        if(dto == null) return null;

        Product product = new Product();
        product.setId(dto.getId() != null ? java.util.UUID.fromString(dto.getId()) : null);
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        // category should be set in service using repository
        return product;
    }
}
