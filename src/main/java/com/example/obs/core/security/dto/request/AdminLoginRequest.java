package com.example.obs.core.security.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminLoginRequest {
    
    @NotBlank(message = "E-posta adresi boş olamaz")
    @Email(message = "Geçerli bir e-posta adresi girilmelidir")
    private String username;
    
    @NotBlank(message = "Şifre alanı boş olamaz")
    private String password;
} 