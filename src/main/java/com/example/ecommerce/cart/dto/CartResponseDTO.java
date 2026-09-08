package com.example.ecommerce.cart.dto;

import com.example.ecommerce.cart.entity.Cart;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartResponseDTO {
    private Long cartId;
    private Long userId;
    private List<CartItemDTO> cartItemsDTOS;

    public CartResponseDTO(Long cartId, Long userId, List<CartItemDTO> cartItemsDTOS) {
        this.cartId = cartId;
        this.userId = userId;
        this.cartItemsDTOS = cartItemsDTOS;
    }
    public CartResponseDTO(Cart cart) {
        this.cartId = cart.getId();
        this.userId = cart.getUser().getUserId();
        this.cartItemsDTOS = cart.getCartItems().stream().map(CartItemDTO::new).toList();
    }

}
