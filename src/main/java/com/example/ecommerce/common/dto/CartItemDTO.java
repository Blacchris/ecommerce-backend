package com.example.ecommerce.common.dto;

import com.example.ecommerce.common.entity.CartItems;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemDTO {
    private Long id;
    private String name;
    private double price;
    private Long quantity;

    public CartItemDTO(CartItems cartItem) {
        this.id = cartItem.getId();
        this.name = cartItem.getProduct().getName();
        this.price = cartItem.getProduct().getPrice();
        this.quantity = cartItem.getQuantity();
    }
}
