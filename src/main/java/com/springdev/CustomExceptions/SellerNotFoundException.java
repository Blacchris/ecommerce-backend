package com.springdev.CustomExceptions;

import org.springframework.beans.factory.config.RuntimeBeanReference;

public class SellerNotFoundException extends RuntimeException {

    public SellerNotFoundException(String message){
        super(message);
    }

    public SellerNotFoundException(String message, Throwable cause){
        super(message, cause);
    }
}
