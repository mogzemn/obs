package com.example.obs.business.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacultyCreateRequest {
    @NotBlank(message = "Fakülte adı boş olamaz")
    @Size(max = 100, message = "Fakülte adı 100 karakterden fazla olamaz")
    private String facultyName;

    private Long deanId;

    @NotNull(message = "Aktiflik durumu boş olamaz")
    private Boolean isActive;
}
