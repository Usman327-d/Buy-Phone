package com.buyPhone.service.impl;

import com.buyPhone.dto.CategoryDTO;
import com.buyPhone.entity.Category;
import com.buyPhone.exception.ResourceNotFoundException;
import com.buyPhone.mapper.CategoryMapper;
import com.buyPhone.repository.CategoryRepository;
import com.buyPhone.service.interfac.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

    private final CategoryRepository repository;
    private final CategoryMapper mapper;


    @Override
    @Transactional
    public CategoryDTO createCategory(CategoryDTO dto) {
        Category category = mapper.toEntity(dto);

        // Handle the parent link for subcategories (iPhone, Samsung, etc.)
        if (dto.getParentId() != null) {
            Category parent = repository.findById(dto.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent category not found"));
            category.setParent(parent);
        }

        Category saved = repository.save(category);
        return mapper.toDTO(saved);
    }

    @Override
    public CategoryDTO getCategory(UUID id) {
        Category category = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", id.toString()));
        return mapper.toDTO(category);
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        return repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDTO updateCategory(UUID id, CategoryDTO dto) {
        Category category = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", id.toString()));
        category.setName(dto.getName());
        return mapper.toDTO(repository.save(category));
    }

    @Override
    public void deleteCategory(UUID id) {
        if(!repository.existsById(id))
            throw new ResourceNotFoundException("Category", id.toString());
        repository.deleteById(id);
    }

    // get distinct category name for search and add purpose
    @Override
    public List<String> getDistinctCategoryNames() {
       return repository.getDistinctCategoryNames();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> getFullCategoryTree() {
        // Fetch root categories (where parent is null)
        List<Category> roots = repository.findByParentIsNull();

        return roots.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }
}
