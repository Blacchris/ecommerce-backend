package com.example.ecommerce.common.service;


import com.example.ecommerce.common.dto.OrderItemDTO;
import com.example.ecommerce.common.dto.OrderResponseDTO;
import com.example.ecommerce.common.entity.Order;
import com.example.ecommerce.common.entity.OrderItem;
import com.example.ecommerce.common.entity.Product;
import com.example.ecommerce.common.entity.User;
import com.example.ecommerce.common.exception.OrderNotCancelled;
import com.example.ecommerce.common.exception.OrderNotFoundException;
import com.example.ecommerce.common.exception.UserNotFoundException;
import com.example.ecommerce.common.repository.OrderItemsRepository;
import com.example.ecommerce.common.repository.OrderRepository;
import com.example.ecommerce.common.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    //private final OrderItemsRepository orderItemsRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrderService(OrderItemsRepository orderItemsRepository, OrderRepository orderRepository, UserRepository userRepository) {
        //this.orderItemsRepository = orderItemsRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    public List<OrderResponseDTO> getAllOrder(Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found."));
        List<Order> orders = user.getOrders();
        if(orders.isEmpty())
            throw new OrderNotFoundException("No order found.");
        List<OrderResponseDTO> newList = new ArrayList<>();
        for(Order item : orders){
            newList.add(new OrderResponseDTO(item));
        }
        return newList;
    }

    public OrderResponseDTO getOneOrder(Long userId, Long orderId){
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found."));
        Order order = user.getOrder(orderId);
        if(order == null)
            throw new OrderNotFoundException("Order not found.");
        return new OrderResponseDTO(order);
    }


    @Transactional
    public void cancelOder(Long userId, Long orderId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found."));
        Order order = user.getOrder(orderId);
        if(order!= null && order.getStatus().equals(Order.OrderStatus.SHIPPED) || order.getStatus().equals(Order.OrderStatus.PAID)){
            throw new OrderNotCancelled("Can not cancel this order.");
        }

        for(OrderItem item : order.getOrderItems()){
            Product product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
        }
        order.setStatus(Order.OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    public List<OrderItemDTO> getOrderItems(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(()-> new OrderNotFoundException("Order not found."));
        List<OrderItemDTO> newItems = new ArrayList<>();
        for(OrderItem item : order.getOrderItems()){
            OrderItemDTO product = new OrderItemDTO(item);
            newItems.add(product);
        }
        return newItems;
    }
}
