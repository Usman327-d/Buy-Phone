package com.buyPhone.service.impl;

import com.buyPhone.dto.ProductDTO;
import com.buyPhone.entity.Product;
import com.buyPhone.exception.ResourceNotFoundException;
import com.buyPhone.mapper.ProductMapper;
import com.buyPhone.repository.ProductRepository;
import com.buyPhone.service.interfac.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper = new ProductMapper();



    @Override
    public ProductDTO createProduct(ProductDTO dto) {
        Product product = mapper.toEntity(dto);
        return mapper.toDTO(repository.save(product));
    }

    @Override
    public ProductDTO getProduct(UUID id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id.toString()));
        return mapper.toDTO(product);
    }

    @Override
    public List<ProductDTO> getProductsByCategory(UUID categoryId) {
        return repository.findByCategoryId(categoryId)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> searchProducts(String keyword) {
        return repository.searchProducts(keyword)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO updateProduct(UUID id, ProductDTO dto) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id.toString()));
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        return mapper.toDTO(repository.save(product));
    }

    @Override
    public void deleteProduct(UUID id) {
        if(!repository.existsById(id))
            throw new ResourceNotFoundException("Product", id.toString());
        repository.deleteById(id);
    }
}
