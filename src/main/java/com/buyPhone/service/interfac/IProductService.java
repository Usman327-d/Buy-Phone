package com.buyPhone.service.interfac;

import com.buyPhone.dto.ProductAdminDTO;
import com.buyPhone.dto.ProductDetailDTO;
import com.buyPhone.dto.ProductSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;


public interface IProductService {
    // Write Operations
    ProductDetailDTO createProduct(MultipartFile image, ProductDetailDTO dto);
    ProductDetailDTO updateProduct(UUID id, ProductDetailDTO dto);
    void deleteProduct(UUID id);
    void updateStock(UUID productId, Integer quantity); // For inventory management

    // Read Operations (Customer)
    Page<ProductSummaryDTO> getAllProductSummary(Pageable pageable); // Paginated!
    ProductDetailDTO getProductDetails(UUID id);
    Page<ProductDetailDTO> searchProducts(String keyword, Pageable pageable);
    Page<ProductDetailDTO> getProductsByCategory(UUID categoryId, Pageable pageable);

    // Read Operations (Admin)
    Page<ProductAdminDTO> getAllProductsForAdmin(Pageable pageable);
}

