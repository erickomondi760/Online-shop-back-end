package com.ecommerce.project.exceptionHandler;

public class ResourceNotFoundException extends RuntimeException{

    String message;

    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(String message) {
        super(message);
        this.message = message;
    }

}
