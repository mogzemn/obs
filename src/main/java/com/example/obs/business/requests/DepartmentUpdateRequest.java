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
public class DepartmentUpdateRequest {
    @NotNull(message = "Bölüm ID'si boş olamaz")
    private Long id;

    @Size(max = 100, message = "Bölüm adı 100 karakterden fazla olamaz")
    private String departmentName;

    @Size(min = 2, max = 2, message = "Bölüm kodu tam olarak 2 karakter olmalıdır")
    @Pattern(regexp = "^[0-9]+$", message = "Bölüm kodu sadece rakamlardan oluşmalıdır")
    private String departmentCode;

    private Long facultyId;

    private Long headOfDepartmentId;

    private Boolean isActive;
}