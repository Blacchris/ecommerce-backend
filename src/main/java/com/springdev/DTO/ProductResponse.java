package com.springdev.DTO;

import lombok.*;

import java.math.BigDecimal;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {

    private long id;
    private String name;
    private BigDecimal price;
    private int stock;

}
