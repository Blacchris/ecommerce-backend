package com.springdev.CustomExceptions;

public class RoleCannotBeEmptyException extends RuntimeException{

    public RoleCannotBeEmptyException(String message){
        super(message);
    }

    public RoleCannotBeEmptyException(String message, Throwable cause){
        super(message, cause);
    }
}
