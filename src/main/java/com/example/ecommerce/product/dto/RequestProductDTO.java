package com.example.ecommerce.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
public class RequestProductDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @Size(min = 3, max = 500, message = "Description must be between 3 and 500 characters")
    @NotBlank(message = "Description is required")
    private String description;

    @NotNull
    @PositiveOrZero(message = "Price is required and cannot be negative")
    private BigDecimal price;

    @NotNull
    @PositiveOrZero(message = "Stock is required and cannot be negative")
    private Integer stock;
}
