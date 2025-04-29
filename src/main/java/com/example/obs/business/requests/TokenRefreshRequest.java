package com.example.obs.business.requests;

import jakarta.validation.constraints.NotBlank;

public class TokenRefreshRequest {
    @NotBlank(message = "Yenileme tokenı boş bırakılamaz")
    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}