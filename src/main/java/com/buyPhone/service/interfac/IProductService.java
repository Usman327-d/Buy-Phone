package com.buyPhone.service.interfac;

import com.buyPhone.dto.ProductDTO;

import java.util.List;
import java.util.UUID;


public interface IProductService {

    ProductDTO createProduct(ProductDTO dto);

    ProductDTO getProduct(UUID id);

    List<ProductDTO> getProductsByCategory(UUID categoryId);

    List<ProductDTO> searchProducts(String keyword);

    ProductDTO updateProduct(UUID id, ProductDTO dto);

    void deleteProduct(UUID id);
}

