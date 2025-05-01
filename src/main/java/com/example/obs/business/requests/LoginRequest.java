package com.example.obs.business.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    
    @NotBlank(message = "Kullanıcı numarası boş olamaz")
    private String userNumber;

    @NotBlank(message = "Şifre boş olamaz")
    private String password;

    public String getUserNumber() {
        return this.userNumber;
    }
    
    public String getPassword() {
        return this.password;
    }
} 