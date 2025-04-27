package com.example.obs.business.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentCreateRequest {

    @NotBlank(message = "Bölüm adı boş olamaz")
    @Size(max = 100, message = "Bölüm adı 100 karakterden fazla olamaz")
    private String departmentName;

    @NotBlank(message = "Bölüm kodu boş olamaz")
    @Size(max = 6, message = "Bölüm kodu 6 karakterden fazla olamaz")
    private String departmentCode;

    @NotNull(message = "Fakülte ID boş olamaz")
    private Long facultyId;

    private Long headId;

    @NotNull(message = "Aktiflik durumu boş olamaz")
    private Boolean isActive;
}