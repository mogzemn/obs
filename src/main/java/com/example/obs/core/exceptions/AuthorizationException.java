package com.example.obs.core.exceptions;

public class AuthorizationException extends RuntimeException {
    public AuthorizationException(String message) {
        super(message);
    }
    
    public static AuthorizationException forOperation(String operation, String resourceType) {
        return new AuthorizationException(resourceType + " için " + operation + " yetkisine sahip değilsiniz");
    }
} 