package com.example.ecommerce.common.dto;

import com.example.ecommerce.common.entity.Order;
import com.example.ecommerce.common.entity.OrderItem;
import com.example.ecommerce.common.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor

public class OrderItemDTO {
    private Long id;

    private Long orderId;

    private Product product;

    private Long quantity;
    private double price;

    public OrderItemDTO(OrderItem orderItem) {
        this.id = orderItem.getId();
        this.orderId = orderItem.getOrder().getOrderId();
        this.product = orderItem.getProduct();
        this.quantity = orderItem.getQuantity();
        this.price = orderItem.getPrice();

    }
}
