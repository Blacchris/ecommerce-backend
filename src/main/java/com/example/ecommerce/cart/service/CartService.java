package com.example.ecommerce.cart.service;

import com.example.ecommerce.cart.dto.OrderResponseDTO;
import com.example.ecommerce.cart.entity.*;
import com.example.ecommerce.common.exception.CartItemNotFoundException;
import com.example.ecommerce.common.exception.CartNotFoundException;
import com.example.ecommerce.common.exception.ProductNotFoundException;
import com.example.ecommerce.common.exception.UserNotFoundException;
import com.example.ecommerce.cart.repository.CartRepository;
import com.example.ecommerce.cart.dto.CartItemDTO;
import com.example.ecommerce.cart.dto.CartResponseDTO;
import com.example.ecommerce.cart.repository.OrderRepository;
import com.example.ecommerce.cart.repository.ProductRepository;
import com.example.ecommerce.cart.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;

    public CartService(UserRepository userRepository, ProductRepository productRepository, CartRepository cartRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
    }


    public List<CartItemDTO> convertItemToDTO(List<CartItems> items){
        List<CartItemDTO> list = new ArrayList<>();
        for(CartItems item : items){
            CartItemDTO itemDTO = new CartItemDTO(item);
            list.add(itemDTO);
        }
        return list;
    }

    public CartResponseDTO getCart(Long userId){
        User user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("user not found."));
        Cart cart = user.getCart();
        if(cart == null){
            throw new CartNotFoundException("Cart not found.");
        }
        return new CartResponseDTO(cart.getId(),user.getUserId(), convertItemToDTO(cart.getCartItems()));

    }




   @Transactional
    public List<CartItemDTO> addToCart(Long userId, Long productId, Long quantity) {
       User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("user not found."));
       Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("product not found."));
       Cart cart = user.getCart();
       if (cart == null) {
           cart = new Cart();
           cart.setUser(user);
           user.setCart(cart);
       }


        if(product.getStock() < quantity)
            throw new ProductNotFoundException("Product Out of stock.");


       for (CartItems existingItem : cart.getCartItems()) {
           if (existingItem.getProduct().getId().equals(product.getId())){
               existingItem.setQuantity(existingItem.getQuantity() + quantity);
           cartRepository.save(cart);
           return convertItemToDTO(cart.getCartItems());
       }
   }
        CartItems newItem = new CartItems(product, quantity);
       newItem.setCart(cart);
       newItem.setQuantity(quantity);
        cart.getCartItems().add(newItem);
        cartRepository.save(cart);
        return convertItemToDTO(cart.getCartItems());
    }


    @Transactional
    public void removeFromCart(Long userId, Long itemId){
        User user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("user not found."));
        Cart cart = user.getCart();
        if(cart == null){
            throw new CartNotFoundException("Cart not found.");
        }

        boolean removed = cart.getCartItems().removeIf(item-> itemId.equals(item.getId()));
        if(!removed){
          throw new CartItemNotFoundException("Cart item not found.");
        }
            cartRepository.save(cart);
    }

    public List<CartItemDTO> updateQuantity(Long userId, Long productId, Long quantity) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("user not found."));
        productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("product not found."));
        Cart cart = user.getCart();
        if(cart == null)
            throw new CartNotFoundException("Cart not found.");

        boolean found =false;
        for(CartItems item : cart.getCartItems()) {
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(quantity);
                found = true;
                break;
            }
        }
        if(!found)
            throw new CartItemNotFoundException("Item not found.");

        cartRepository.save(cart);
        return convertItemToDTO(cart.getCartItems());
    }


    public void clearCart(Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("user not found."));
        Cart cart = user.getCart();
        if(cart == null)
            throw new CartNotFoundException("Cart not found.");
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }

    public BigDecimal calculateTotal(Long userId){
        User user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("User not Found."));
        Cart cart = user.getCart();
        if(cart == null)
            throw new CartNotFoundException("Cart not found");
        if(cart.getCartItems().isEmpty()){
            throw new CartItemNotFoundException("Cart empty.");
        }
        BigDecimal total = BigDecimal.ZERO;
        for(CartItems item : cart.getCartItems()){
            total = item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
        }
        return total;
    }

    private OrderItem convertCartToOrder(CartItems item){
        return new OrderItem(item);
    }

    //ORDER ACTION


    @Transactional
    public OrderResponseDTO order(Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("user not found."));
        Cart cart = user.getCart();

        if(cart == null || cart.getCartItems() == null || cart.getCartItems().isEmpty()){ throw new IllegalStateException("Cart is empty."); }

        Order order = new Order();
        order.setUser(user);
        order.setStatus(Order.OrderStatus.PENDING);

        //BigDecimal totalPrice = BigDecimal.ZERO;

        for(CartItems item : cart.getCartItems()){
            OrderItem orderItem = convertCartToOrder(item);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);

         //   totalPrice = totalPrice.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }


        BigDecimal totalPrice = cart.getCartItems().stream().map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))).reduce(BigDecimal.ZERO,BigDecimal::add);

        order.setTotalAmount(totalPrice);

        Order savedOrder = orderRepository.save(order);
        cart.getCartItems().clear();
        cartRepository.save(cart);
        return new OrderResponseDTO(savedOrder);
    }

}
