package com.example.obs.core.utilities.results;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private T data;
    private boolean success;
    private String message;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data, true, "İşlem başarılı");
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(data, true, message);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(null, false, message);
    }
} 