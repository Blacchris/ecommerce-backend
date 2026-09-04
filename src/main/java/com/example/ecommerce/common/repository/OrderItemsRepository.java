package com.example.ecommerce.common.repository;
import com.example.ecommerce.common.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemsRepository extends JpaRepository<OrderItem,Long> {
}
