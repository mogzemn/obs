package com.example.obs.business.requests;

import com.example.obs.model.enums.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

    private int id;

    @Size(min = 11, max = 100, message = "Şifre en az 11 ,en fazla 100 karakter olmalıdır")
    private String password;

    @Size(max = 50, message = "Ad 50 karakterden fazla olamaz")
    private String firstName;

    @Size(max = 50, message = "Soyad 50 karakterden fazla olamaz")
    private String lastName;

    @Size(max = 12, message = "Telefon 12 karakterden fazla olamaz")
    @Pattern(regexp = "^[0-9+\\s-]+$", message = "Geçersiz telefon formatı")
    private String phone;

    @Email(message = "Geçerli bir e-posta adresi giriniz")
    @Size(max = 50, message = "E-posta 50 karakterden fazla olamaz")
    private String email;

    private UserStatus status;

    private Boolean isActive;

    private Boolean hasLoginPermission;

    private Boolean isAdmin;
}