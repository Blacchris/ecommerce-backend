package com.example.ecommerce.cart.dto;

import com.example.ecommerce.cart.entity.CartItems;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CartItemDTO {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long quantity;

    public CartItemDTO(CartItems cartItem) {
        this.id = cartItem.getId();
        this.name = cartItem.getProduct().getName();
        this.price = cartItem.getProduct().getPrice();
        this.quantity = cartItem.getQuantity();
    }
}
