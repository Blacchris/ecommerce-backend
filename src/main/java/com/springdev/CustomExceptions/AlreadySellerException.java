package com.springdev.CustomExceptions;

public class AlreadySellerException extends RuntimeException{

    public AlreadySellerException(String message){
        super(message);
    }

    public AlreadySellerException(String message, Throwable cause){
        super(message, cause);
    }
}
