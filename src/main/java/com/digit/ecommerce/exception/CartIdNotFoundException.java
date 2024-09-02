package com.digit.ecommerce.exception;

public class CartIdNotFoundException extends RuntimeException{
    public CartIdNotFoundException(String message){
        super(message);
    }
}

