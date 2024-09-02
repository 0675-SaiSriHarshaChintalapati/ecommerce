package com.digit.ecommerce.exception;

public class BookLimitException extends RuntimeException{
    public BookLimitException(String message) {
        super(message);
    }
}
