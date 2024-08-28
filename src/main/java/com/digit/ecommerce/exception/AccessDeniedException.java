package com.digit.ecommerce.exception;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String message){
        super(message);
    }

}
