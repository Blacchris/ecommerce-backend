package com.example.ecommerce.common.exception;

public class CartItemNotFoundException extends RuntimeException{

    public CartItemNotFoundException(String message){
        super(message);
    }
}
