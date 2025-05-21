package com.example.obs.core.security.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentLoginRequest {
    
    @NotBlank(message = "Öğrenci numarası boş olamaz")
    private String username;
    
    @NotBlank(message = "Şifre alanı boş olamaz")
    private String password;
} 