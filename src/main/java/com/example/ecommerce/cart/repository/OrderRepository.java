package com.example.ecommerce.cart.repository;

import com.example.ecommerce.cart.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
