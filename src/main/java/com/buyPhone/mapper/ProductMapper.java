package com.buyPhone.mapper;

import com.buyPhone.dto.ProductDetailDTO;
import com.buyPhone.entity.Product;
import com.buyPhone.dto.ProductAdminDTO;
import com.buyPhone.dto.ProductSummaryDTO;
import org.springframework.stereotype.Component;


@Component
public class ProductMapper {

    // 1. LIGHTWEIGHT: For Customer List Views
    public ProductSummaryDTO toSummaryDTO(Product product) {
        if (product == null) return null;

        return ProductSummaryDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .build();
    }

    // 2. DETAILED: For Customer Product Pages
    public ProductDetailDTO toDetailDTO(Product product) {
        if (product == null) return null;

        return ProductDetailDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .createdAt(product.getCreatedAt())
                .attributes(product.getAttributes())
                .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
                .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                .build();
    }

    // 3. ADMIN: Full View with Inventory
    public ProductAdminDTO toAdminDTO(Product product) {
        if (product == null) return null;

        // Start with Detail DTO data
        ProductDetailDTO detail = toDetailDTO(product);

        ProductAdminDTO adminDto = new ProductAdminDTO();
        // Copy fields from detail (using a helper or manual set)
        adminDto.setId(detail.getId());
        adminDto.setName(detail.getName());
        adminDto.setDescription(detail.getDescription());
        adminDto.setPrice(detail.getPrice());
        adminDto.setImageUrl(detail.getImageUrl());
        adminDto.setCreatedAt(detail.getCreatedAt());
        adminDto.setAttributes(detail.getAttributes());
        adminDto.setCategoryId(detail.getCategoryId());
        adminDto.setCategoryName(detail.getCategoryName());

        // Add Admin specific fields
        if (product.getInventory() != null) {
            // Assuming you have an InventoryMapper or simple conversion
            adminDto.setInitialQuantity(product.getInventory().getQuantity());
        }

        return adminDto;
    }

    // 4. ENTITY: Convert Input DTO back to Database Object
    public Product toEntity(ProductDetailDTO dto) {
        if (dto == null) return null;

        Product product = new Product();
        // Handle UUID safety: don't convert if null or empty
        if (dto.getId() != null) {
            product.setId(dto.getId());
        }

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setAttributes(dto.getAttributes());
        product.setImageUrl(dto.getImageUrl());

        return product;
    }
}