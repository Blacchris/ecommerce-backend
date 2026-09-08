package com.example.ecommerce.cart.repository;
import com.example.ecommerce.cart.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemsRepository extends JpaRepository<OrderItem,Long> {
}
