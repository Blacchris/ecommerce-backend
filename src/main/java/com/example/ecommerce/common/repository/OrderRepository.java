package com.example.ecommerce.common.repository;

import com.example.ecommerce.common.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
