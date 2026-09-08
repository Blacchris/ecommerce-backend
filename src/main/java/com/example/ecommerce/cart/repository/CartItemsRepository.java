package com.example.ecommerce.cart.repository;

import com.example.ecommerce.cart.entity.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemsRepository extends JpaRepository<CartItems,Long> {
}
