package com.example.ecommerce.common.service;

import com.example.ecommerce.cart.dto.CartResponseDTO;
import com.example.ecommerce.cart.entity.Cart;
import com.example.ecommerce.cart.entity.User;
import com.example.ecommerce.cart.repository.CartRepository;
import com.example.ecommerce.cart.repository.ProductRepository;
import com.example.ecommerce.cart.repository.UserRepository;
import com.example.ecommerce.cart.service.CartService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartService cartService;

    @Test
    void shouldReturnCartWhenUserAndCartExit(){
        Long userId = 1L;
        User user = new User();
        Cart cart = new Cart();
        cart.setId(100L);
        user.setCart(cart);
        cart.setUser(user);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        CartResponseDTO result = cartService.getCart(userId);

        assertEquals(100L, result.getCartId());

        verify(userRepository).findById(userId);
    }


    @Test
    void shouldThrowUserNotFoundExceptionWhenUserDoesNotExist(){
    }
}
