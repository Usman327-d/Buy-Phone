package com.buyPhone.service.interfac;


import com.buyPhone.dto.CategoryDTO;

import java.util.List;
import java.util.UUID;


public interface ICategoryService {

    CategoryDTO createCategory(CategoryDTO dto);

    CategoryDTO getCategory(UUID id);

    List<CategoryDTO> getAllCategories();

    CategoryDTO updateCategory(UUID id, CategoryDTO dto);

    void deleteCategory(UUID id);

    List<String> getDistinctCategoryNames();
}

