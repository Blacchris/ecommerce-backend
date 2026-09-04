package com.example.ecommerce.common.repository;

import com.example.ecommerce.common.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
