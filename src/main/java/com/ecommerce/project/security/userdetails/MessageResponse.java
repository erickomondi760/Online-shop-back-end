package com.ecommerce.project.security.userdetails;

import lombok.Getter;

@Getter
public class MessageResponse {
    private String message;
    public MessageResponse(String s) {
        this.message = s;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
