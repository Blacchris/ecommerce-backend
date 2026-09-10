package com.example.ecommerce.common.exception;

public class CartNotFoundException extends RuntimeException{

    public CartNotFoundException(String message){
        super(message);
    }
}
