package com.example.ecommerce.common.controller;


import com.example.ecommerce.common.dto.OrderItemDTO;
import com.example.ecommerce.common.dto.OrderResponseDTO;
import com.example.ecommerce.common.repository.OrderRepository;
import com.example.ecommerce.common.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/order")
public class OrderController {
    private final OrderService orderService;
    private final OrderRepository orderRepository;

    public OrderController(OrderService orderService, OrderRepository orderRepository) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders(@PathVariable Long userId){
        List<OrderResponseDTO> all = orderService.getAllOrder(userId);
        return ResponseEntity.ok(all);
    }

    @GetMapping("/{userId}/{orderId}")
    public ResponseEntity<OrderResponseDTO> getOneOrder(@PathVariable Long userId, @PathVariable Long orderId){
        OrderResponseDTO response = orderService.getOneOrder(userId,orderId);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{userId}/{orderId}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long userId, @PathVariable Long orderId){
        orderService.cancelOder(userId,orderId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/items/{orderId}")
    public ResponseEntity<List<OrderItemDTO>> getOrderItems(@PathVariable Long orderId) {
        List<OrderItemDTO> items = orderService.getOrderItems(orderId);
        return ResponseEntity.ok(items);
    }
}
