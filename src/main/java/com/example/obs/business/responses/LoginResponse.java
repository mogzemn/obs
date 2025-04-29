package com.example.obs.business.responses;

public class LoginResponse {
    private String token;
    private String type;
    private String refreshToken;

    public LoginResponse(String token, String type) {
        this.token = token;
        this.type = type;
    }

    public LoginResponse(String token, String type, String refreshToken) {
        this.token = token;
        this.type = type;
        this.refreshToken = refreshToken;
    }

    // Getters
    public String getToken() {
        return token;
    }

    public String getType() {
        return type;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}