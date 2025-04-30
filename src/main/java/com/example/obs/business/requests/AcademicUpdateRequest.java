package com.example.obs.business.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcademicUpdateRequest {
    @NotNull(message = "Akademisyen ID'si boş olamaz")
    private int id;

    @Valid
    private UserUpdateRequest user;

    private int departmentId;

    private String title;

    private Boolean isActive;
}