package com.example.ecommerce.common.dto;

import com.example.ecommerce.common.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {

    private Long orderId;

    private Long userId;

    private List<OrderItemDTO> orderItems;

    private double totalAmount;

    private Order.OrderStatus status;

    private LocalDateTime createdAt;

    public OrderResponseDTO(Order order){
        this.orderId = order.getOrderId();
        this.userId = order.getUser().getUserId();
        this.orderItems = order.getOrderItems().stream().map(OrderItemDTO::new).toList();
        this.totalAmount = order.getTotalAmount();
        this.status = order.getStatus();
        this.createdAt = order.getCreatedAt();

    }
}
