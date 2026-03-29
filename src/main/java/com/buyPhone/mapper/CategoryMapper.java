package com.buyPhone.mapper;

import com.buyPhone.dto.CategoryDTO;
import com.buyPhone.entity.Category;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {

    public CategoryDTO toDTO(Category category) {
        if (category == null) return null;

        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                // Map the parent's ID to the DTO
                .parentId(category.getParent() != null ? category.getParent().getId() : null)
                // Recursively map all children
                .children(category.getChildren() != null ?
                        category.getChildren().stream()
                                .map(this::toDTO)
                                .collect(Collectors.toList())
                        : new ArrayList<>())
                .build();
    }

    public Category toEntity(CategoryDTO dto) {
        if (dto == null) return null;

        Category category = new Category();
        category.setId(dto.getId());
        category.setName(dto.getName());

        // Note: Parent and Children relationships are usually handled
        // in the Service layer to ensure they are attached to the Hibernate Session.
        return category;
    }
}
