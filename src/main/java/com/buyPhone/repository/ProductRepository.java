package com.buyPhone.repository;

import com.buyPhone.dto.ProductSummaryDTO;
import com.buyPhone.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    // LIKE for case-insensitive searching in PostgreSQL
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :kw, '%')) " +
            "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :kw, '%'))")
    Page<Product> searchByNameOrDescription(@Param("kw") String keyword, Pageable pageable);

    // JOIN FETCH prevents N+1 queries when loading the list
    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.category WHERE p.category.id = :catId")
    Page<Product> findByCategoryId(@Param("catId") UUID categoryId, Pageable pageable);

    @Query("SELECT p FROM Product p JOIN FETCH p.inventory JOIN FETCH p.category")
    Page<Product> findAllWithDetails(Pageable pageable);

    @Query("SELECT new com.buyPhone.dto.ProductSummaryDTO(p.id, p.name, p.price, p.imageUrl) " +
            "FROM Product p")
    Page<ProductSummaryDTO> findAllProductSummary(Pageable pageable);
}
