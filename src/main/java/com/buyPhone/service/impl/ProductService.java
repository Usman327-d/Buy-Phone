package com.buyPhone.service.impl;

import com.buyPhone.dto.ProductAdminDTO;
import com.buyPhone.dto.ProductDetailDTO;
import com.buyPhone.dto.ProductSummaryDTO;
import com.buyPhone.entity.Category;
import com.buyPhone.entity.Inventory;
import com.buyPhone.entity.Product;
import com.buyPhone.exception.AppException;
import com.buyPhone.exception.CloudServiceException;
import com.buyPhone.exception.ResourceNotFoundException;
import com.buyPhone.mapper.CategoryMapper;
import com.buyPhone.mapper.InventoryMapper;
import com.buyPhone.mapper.ProductMapper;
import com.buyPhone.repository.CategoryRepository;
import com.buyPhone.repository.InventoryRepository;
import com.buyPhone.repository.ProductRepository;
import com.buyPhone.service.interfac.IProductService;
import com.buyPhone.service.interfac.IUploadProductImageService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final IUploadProductImageService uploadProductImageService;
    private final InventoryRepository inventoryRepository;

    private final ProductMapper productMapper;

    private final InventoryMapper inventoryMapper = new InventoryMapper();
    private final CategoryMapper categoryMapper = new CategoryMapper();



    @Override
    @Transactional
    public ProductDetailDTO createProduct(MultipartFile image, ProductDetailDTO dto) {

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found", dto.getId().toString()));

        // map product
        Product product = productMapper.toEntity(dto);
        product.setCategory(category);

        // image upload
        Map<String, Object> cloudResponse =  uploadProductImageService.uploadImage(image, "Products");

        Object objUrl = cloudResponse.get("secure_url");
        if(cloudResponse == null || objUrl == null){
            throw new AppException("Image Cloud is down or sending response null");
        }

        // extract imageUrl from cloud response
        String imageUrl = objUrl.toString();
        product.setImageUrl(imageUrl);

        // inventory
        Inventory inventory = new Inventory();
        inventory.setQuantity(dto.getInitialQuantity() != null ? dto.getInitialQuantity() : 0);
        inventory.setProduct(product);
        product.setInventory(inventory);

        Product savedProduct = productRepository.save(product);

        return productMapper.toDetailDTO(savedProduct);
    }

    @Override
    @Transactional
    public ProductDetailDTO updateProduct(UUID id, ProductDetailDTO dto) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found", id.toString()));

        existingProduct.setName(dto.getName());
        existingProduct.setDescription(dto.getDescription());
        existingProduct.setPrice(dto.getPrice());
        existingProduct.setAttributes(dto.getAttributes());

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found", dto.getCategoryId().toString()));
            existingProduct.setCategory(category);
        }

        return productMapper.toDetailDTO(productRepository.save(existingProduct));
    }

    @Override
    @Transactional
    public void updateStock(UUID productId, Integer quantity) {
        // High performance: update quantity directly in DB to avoid race conditions
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found with ID: " , productId.toString());
        }

        int updatedRows = inventoryRepository.updateQuantity(productId, quantity);

        if (updatedRows == 0) {
            // This might happen if the Product exists but the Inventory record is missing
            throw new ResourceNotFoundException("Inventory record missing for product: " , productId.toString());
        }
    }


    @Override
    @Transactional(readOnly = true)
    public Page<ProductSummaryDTO> getAllProductSummary(Pageable pageable) {
        return productRepository.findAllProductSummary(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDetailDTO getProductDetails(UUID id) {
        return productRepository.findById(id)
                .map(productMapper::toDetailDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found", id.toString()));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDetailDTO> searchProducts(String keyword, Pageable pageable) {
        return productRepository.searchByNameOrDescription(keyword, pageable)
                .map(productMapper::toDetailDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDetailDTO> getProductsByCategory(UUID categoryId, Pageable pageable) {
        return productRepository.findByCategoryId(categoryId, pageable)
                .map(productMapper::toDetailDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductAdminDTO> getAllProductsForAdmin(Pageable pageable) {
        // Uses a specialized repository call to FETCH JOIN inventory and category
        return productRepository.findAllWithDetails(pageable)
                .map(productMapper::toAdminDTO);
    }

    @Override
    @Transactional
    public void deleteProduct(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found", id.toString()));

        if (product.getImageUrl() != null) {
            boolean isDeleted = uploadProductImageService.deleteImage(product.getImageUrl());

            if (!isDeleted) {
                // This will trigger your GlobalExceptionHandler
                throw new CloudServiceException("Could not delete product image from cloud. Transaction aborted.");
            }
        }

        productRepository.delete(product);
    }

}
