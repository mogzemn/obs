package com.example.obs.business.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentCreateRequest {
    @NotBlank(message = "Bölüm adı boş olamaz")
    @Size(max = 100, message = "Bölüm adı 100 karakterden fazla olamaz")
    private String departmentName;

    @NotBlank(message = "Bölüm kodu boş olamaz")
    @Size(min = 3, max = 3, message = "Bölüm kodu tam olarak 3 karakter olmalıdır")
    @Pattern(regexp = "^[0-9]+$", message = "Bölüm kodu sadece rakamlardan oluşmalıdır")
    private String departmentCode;

    @NotNull(message = "Fakülte ID boş olamaz")
    private Long facultyId;

    private Long headOfDepartmentId;

    @NotNull(message = "Aktiflik durumu boş olamaz")
    private Boolean isActive;
}