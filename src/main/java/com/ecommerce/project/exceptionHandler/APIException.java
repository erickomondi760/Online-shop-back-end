package com.ecommerce.project.exceptionHandler;

public class APIException extends RuntimeException{
    private String message;

    public APIException(String message) {
        super(message);
        this.message = message;
    }
}
