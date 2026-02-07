package com.springdev.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class AddProduct {

    private String name;
    private String description;
    private BigDecimal price;
    private int stock;
    private String Category;
}
