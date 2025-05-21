package com.example.obs.business.requests;

import com.example.obs.model.enums.Role;
import com.example.obs.model.enums.UserStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequest {

    @NotBlank(message = "Şifre alanı boş olamaz")
    @Size(min = 11,max = 100, message = "Şifre en az 11 ,en fazla 100 karakter olmalıdır")
    private String password;

    @NotBlank(message = "Ad alanı boş olamaz")
    @Size(max = 50, message = "Ad 50 karakterden fazla olamaz")
    private String firstName;

    @NotBlank(message = "Soyad alanı boş olamaz")
    @Size(max = 50, message = "Soyad 50 karakterden fazla olamaz")
    private String lastName;

    @NotBlank(message = "TC Kimlik numarası boş olamaz")
    @Size(min = 11, max = 11, message = "TC Kimlik numarası 11 karakter olmalıdır")
    @Pattern(regexp = "^[0-9]+$", message = "TC Kimlik numarası sadece rakamlardan oluşmalıdır")
    private String identityNumber;

    @NotNull(message = "Doğum tarihi boş olamaz")
    @Past(message = "Doğum tarihi geçmiş bir tarih olmalıdır")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @NotBlank(message = "Telefon alanı boş olamaz")
    @Size(max = 15, message = "Telefon numarası en fazla 15 karakter olmalıdır.")
    @Pattern(regexp = "^\\+90\\d{10}$", message = "Telefon numarası +90 ile başlamalı ve ardından 10 rakam gelmelidir (örn: +905xxxxxxxxx).")
    private String phone;

    @NotBlank(message = "E-posta alanı boş olamaz")
    @Email(message = "Geçerli bir e-posta adresi giriniz")
    @Size(max = 50, message = "E-posta 50 karakterden fazla olamaz")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "E-posta formatı geçersiz (örn: ornek.eposta@domain.com)")
    private String email;

    @NotNull(message = "Kullanıcı durumu boş olamaz")
    private UserStatus status;

    @NotNull(message = "Aktiflik durumu boş olamaz")
    private Boolean isActive;

    @NotNull(message = "Giriş izni durumu boş olamaz")
    private Boolean hasLoginPermission;
    
    @NotNull(message = "Admin durumu boş olamaz")
    private Boolean isAdmin = false;
    
    @NotNull(message = "Kullanıcı rolü boş olamaz")
    private Role role;
}
