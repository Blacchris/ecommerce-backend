package com.example.ecommerce.common.exception;

public class OrderNotCancelled extends RuntimeException{
    public OrderNotCancelled(String message){
        super(message);
    }

}
