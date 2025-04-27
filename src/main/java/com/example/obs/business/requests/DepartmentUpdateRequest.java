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

    @Size(max = 6, message = "Bölüm kodu 6 karakterden fazla olamaz")
    private String departmentCode;

    private Long facultyId;

    private Long headId;

    private Boolean isActive;
}