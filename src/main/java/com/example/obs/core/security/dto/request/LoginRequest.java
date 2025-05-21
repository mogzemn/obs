package com.example.obs.core.security.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    
    @NotBlank(message = "Kullanıcı adı veya numara alanı boş olamaz")
    private String username;
    
    @NotBlank(message = "Şifre alanı boş olamaz")
    private String password;
    
    @NotBlank(message = "Kullanıcı tipi boş olamaz")
    private String userType;
}
