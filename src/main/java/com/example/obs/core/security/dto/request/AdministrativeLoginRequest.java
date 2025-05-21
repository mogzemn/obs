package com.example.obs.core.security.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdministrativeLoginRequest {
    
    @NotBlank(message = "Personel numarası boş olamaz")
    private String username;
    
    @NotBlank(message = "Şifre alanı boş olamaz")
    private String password;
} 