package com.buyPhone.mapper;

import com.buyPhone.dto.CategoryDTO;
import com.buyPhone.entity.Category;

public class CategoryMapper {

    public CategoryDTO toDTO(Category category){
        if(category == null) return null;

        return CategoryDTO.builder()
                .id(category.getId() != null ? category.getId().toString() : null)
                .name(category.getName())
                .build();
    }

    public Category toEntity(CategoryDTO dto){
        if(dto == null) return null;

        Category category = new Category();
        category.setId(dto.getId() != null ? java.util.UUID.fromString(dto.getId()) : null);
        category.setName(dto.getName());
        return category;
    }
}
