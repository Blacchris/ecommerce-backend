package com.springdev.CustomExceptions;

import com.springdev.DTO.CategoryRequest;

public class CategoryAlreadyExistException extends RuntimeException{

    public CategoryAlreadyExistException(String message){
        super(message);
    }

    public CategoryAlreadyExistException(String message, Throwable cause){
        super(message, cause);
    }
}
