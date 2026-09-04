package com.example.ecommerce.common.exception;

public class UnauthorizedActionException  extends RuntimeException{
    public UnauthorizedActionException (String message){
        super(message);
    }
}
