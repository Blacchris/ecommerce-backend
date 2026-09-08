package com.example.ecommerce.cart.dto;

import com.example.ecommerce.cart.entity.OrderItem;
import com.example.ecommerce.cart.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class OrderItemDTO {
    private Long id;

    private Long orderId;

    private Product product;

    private Long quantity;
    private BigDecimal price;

    public OrderItemDTO(OrderItem orderItem) {
        this.id = orderItem.getId();
        this.orderId = orderItem.getOrder().getOrderId();
        this.product = orderItem.getProduct();
        this.quantity = orderItem.getQuantity();
        this.price = orderItem.getPrice();

    }
}
