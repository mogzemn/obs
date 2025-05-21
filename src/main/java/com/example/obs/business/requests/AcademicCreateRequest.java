package com.example.obs.business.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcademicCreateRequest {
    @Valid
    @NotNull(message = "Kullanıcı bilgileri boş olamaz")
    private UserCreateRequest user;

    @NotNull(message = "Bölüm ID boş olamaz")
    private Long departmentId;

    private String title;

    @NotNull(message = "Aktiflik durumu boş olamaz")
    private Boolean isActive;
}
