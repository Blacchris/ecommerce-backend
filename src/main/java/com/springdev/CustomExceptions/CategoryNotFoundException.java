package com.springdev.CustomExceptions;

import com.springdev.DTO.CategoryResponse;

public class CategoryNotFoundException extends RuntimeException{

    public CategoryNotFoundException(String message){
        super(message);
    }

    public CategoryNotFoundException(String message, Throwable cause){
        super(message, cause);
    }
}
