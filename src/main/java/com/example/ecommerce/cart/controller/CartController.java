package com.example.ecommerce.cart.controller;

import com.example.ecommerce.cart.dto.CartItemDTO;
import com.example.ecommerce.cart.dto.CartResponseDTO;
import com.example.ecommerce.cart.dto.OrderResponseDTO;
import com.example.ecommerce.cart.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/cart")
public class CartController {
    private final CartService service;

    public CartController(CartService service) {
        this.service = service;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartResponseDTO> getCart(@PathVariable Long userId){
        CartResponseDTO response = service.getCart(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add/{userId}/{productId}/{quantity}")
    public ResponseEntity<List<CartItemDTO>> addToCart(@PathVariable Long userId, @PathVariable Long productId, @PathVariable Long quantity){
        List<CartItemDTO> updatedCart = service.addToCart(userId, productId, quantity);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedCart);
    }

    @DeleteMapping("/delete/{userId}/{cartItemId}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Long userId, @PathVariable Long cartItemId){
        service.removeFromCart(userId, cartItemId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{userId}/{productId}/{quantity}")
    public ResponseEntity<List<CartItemDTO>> updateQuantity(@PathVariable Long userId, @PathVariable Long productId, @PathVariable Long quantity){
        List<CartItemDTO> updatedCart = service.updateQuantity(userId, productId, quantity);
       return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId){
        service.clearCart(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/order/{userId}")
    public ResponseEntity<OrderResponseDTO> order(@PathVariable Long userId){
        OrderResponseDTO order = service.order(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
}
