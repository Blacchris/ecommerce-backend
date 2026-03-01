package com.springdev.CustomExceptions;

public class SellerRequestNotFoundException extends RuntimeException{

    public SellerRequestNotFoundException(String message){
        super(message);
    }

    public SellerRequestNotFoundException(String message, Throwable cause){
        super(message, cause);
    }
}
