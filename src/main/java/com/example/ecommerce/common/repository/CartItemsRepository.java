package com.example.ecommerce.common.repository;

import com.example.ecommerce.common.entity.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemsRepository extends JpaRepository<CartItems,Long> {
}
