package com.digit.ecommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<?> resourceAlreadyException(UserAlreadyExistException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> UserAlreadyExistException(AuthenticationException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> AccessDenied(AuthenticationException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}

