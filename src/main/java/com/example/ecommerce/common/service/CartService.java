package com.example.ecommerce.common.service;

import com.example.ecommerce.common.dto.OrderResponseDTO;
import com.example.ecommerce.common.entity.*;
import com.example.ecommerce.common.exception.CartItemNotFoundException;
import com.example.ecommerce.common.exception.CartNotFoundException;
import com.example.ecommerce.common.exception.ProductNotFoundException;
import com.example.ecommerce.common.exception.UserNotFoundException;
import com.example.ecommerce.common.repository.CartRepository;
import com.example.ecommerce.common.dto.CartItemDTO;
import com.example.ecommerce.common.dto.CartResponseDTO;
import com.example.ecommerce.common.repository.ProductRepository;
import com.example.ecommerce.common.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    public CartService(UserRepository userRepository, ProductRepository productRepository, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
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

    public double calculateTotal(Long userId){
        User user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("User not Found."));
        Cart cart = user.getCart();
        if(cart == null)
            throw new CartNotFoundException("Cart not found");
        if(cart.getCartItems().isEmpty()){
            throw new CartItemNotFoundException("Cart empty.");
        }
        double total = 0D;
        for(CartItems item : cart.getCartItems()){
            total += item.getProduct().getPrice() * item.getQuantity();
        }
        return total;
    }

    private OrderItem convertCartToOrder(CartItems item){
        return new OrderItem(item);
    }

    //ORDER ACTION

//    public OrderResponseDTO order(Long userId){
//        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("user not found."));
//        Cart cart = user.getCart();
//        Order order = new Order();
//        for(CartItems item : cart.getCartItems()){
//            order.getOrderItems().add(convertCartToOrder(item));
//        }
//        return new OrderResponseDTO(order);
//    }
//
}
