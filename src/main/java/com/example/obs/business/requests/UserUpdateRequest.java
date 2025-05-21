package com.example.obs.business.requests;

import com.example.obs.model.enums.Role;
import com.example.obs.model.enums.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @Size(min = 11, max = 100, message = "Şifre en az 11 ,en fazla 100 karakter olmalıdır")
    private String password;

    @Size(max = 50, message = "Ad 50 karakterden fazla olamaz")
    private String firstName;

    @Size(max = 50, message = "Soyad 50 karakterden fazla olamaz")
    private String lastName;

    @Size(max = 15, message = "Telefon numarası en fazla 15 karakter olmalıdır.")
    @Pattern(regexp = "^\\+90\\d{10}$", message = "Telefon numarası +90 ile başlamalı ve ardından 10 rakam gelmelidir (örn: +905xxxxxxxxx).")
    private String phone;

    @Email(message = "Geçerli bir e-posta adresi giriniz")
    @Size(max = 50, message = "E-posta 50 karakterden fazla olamaz")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "E-posta formatı geçersiz (örn: ornek.eposta@domain.com)")
    private String email;

    private UserStatus status;

    private Boolean isActive;

    private Boolean hasLoginPermission;
    
    private Boolean isAdmin;

    private Role role;
}
