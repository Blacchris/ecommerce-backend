package com.example.ecommerce.product.entity;


import com.example.ecommerce.common.exception.InvalidRequestException;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "product"
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stock = 0;

    @Column(nullable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;



    public void changeName(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidRequestException("Product name cannot be null or blank");
        }
        this.name = name.trim();
    }

    public void changeDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new InvalidRequestException("Product description cannot be null or blank");
        }
        this.description = description.trim();
    }

    public void changePrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidRequestException("Product price cannot be null or negative");
        }
        this.price = price;
    }


    public void changeStock(Integer stock) {
        if (stock == null || stock < 0) {
            throw new InvalidRequestException("Product stock cannot be null or negative");
        }
        this.stock = stock;
    }


    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
