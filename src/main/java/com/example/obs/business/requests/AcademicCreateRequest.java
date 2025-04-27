package com.example.obs.business.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcademicCreateRequest {

    @Valid
    @NotNull(message = "Kullanıcı bilgileri boş olamaz")
    private UserCreateRequest user;

    @NotNull(message = "Bölüm ID boş olamaz")
    private Long departmentId;

    @NotBlank(message = "Sicil numarası boş olamaz")
    @Size(max = 20, message = "Sicil numarası 20 karakterden fazla olamaz")
    private String registrationNumber;
}