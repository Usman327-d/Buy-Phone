package com.buyPhone.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "products",
        indexes = {
                @Index(name = "idx_product_category", columnList = "category_id"),
                @Index(name = "idx_product_name", columnList = "name")
        }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(columnDefinition = "uuid")
    private UUID id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Double price;

    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(columnDefinition = "jsonb")
    private String attributes;  // dynamic fields, like brand, condition, model

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;


    private String imageUrl;

    @OneToOne(mappedBy = "product")
    private Inventory inventory;

}

