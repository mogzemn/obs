package com.example.obs.business.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacultyUpdateRequest {
    @NotNull(message = "Fakülte ID'si boş olamaz")
    private Long id;

    @Size(max = 100, message = "Fakülte adı 100 karakterden fazla olamaz")
    private String facultyName;

    @Size(min = 2, max = 2, message = "Fakülte kodu tam olarak 2 karakter olmalıdır")
    @Pattern(regexp = "^[0-9]+$", message = "Fakülte kodu sadece rakamlardan oluşmalıdır")
    private String facultyCode;

    private Long deanId;

    private Boolean isActive;
}