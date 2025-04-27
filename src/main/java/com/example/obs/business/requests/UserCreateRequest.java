package com.example.obs.business.requests;

import com.example.obs.model.enums.UserStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequest {

    @NotBlank(message = "Şifre alanı boş olamaz")
    @Size(min = 6, message = "Şifre en az 6 karakter olmalıdır")
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
    private LocalDate birthDate;

    @NotBlank(message = "Telefon alanı boş olamaz")
    @Size(max = 20, message = "Telefon 20 karakterden fazla olamaz")
    @Pattern(regexp = "^[0-9+\\s-]+$", message = "Geçersiz telefon formatı")
    private String phone;

    @NotBlank(message = "E-posta alanı boş olamaz")
    @Email(message = "Geçerli bir e-posta adresi giriniz")
    @Size(max = 100, message = "E-posta 100 karakterden fazla olamaz")
    private String email;

    @NotNull(message = "Kullanıcı durumu boş olamaz")
    private UserStatus status;

    @NotNull(message = "Aktiflik durumu boş olamaz")
    private Boolean isActive;

    @NotNull(message = "Giriş izni durumu boş olamaz")
    private Boolean hasLoginPermission;

    private Boolean isAdmin = false;
}