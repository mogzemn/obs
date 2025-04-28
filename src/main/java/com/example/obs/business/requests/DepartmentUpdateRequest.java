package com.example.obs.business.requests;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentUpdateRequest {

    @Size(max = 100, message = "Bölüm adı 100 karakterden fazla olamaz")
    private String departmentName;

    @Size(min = 3, max = 3, message = "Bölüm kodu 3 karakterden oluşmalıdır")
    private String departmentCode;

    private Long facultyId;

    private Long headId;

    private Boolean isActive;
}