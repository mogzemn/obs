package com.example.obs.core.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
    
    public static NotFoundException forEntity(String entityType, Object id) {
        return new NotFoundException(entityType + " bulunamadı: " + id);
    }
} 