package com.springdev.DTO;

import lombok.Builder;

import java.math.BigDecimal;


@Builder
public class ProductResponse {

    private long id;
    private String name;
    private BigDecimal price;
    private int stock;

}
