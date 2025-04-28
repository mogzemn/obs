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
    private Long id;

    @Valid
    private UserUpdateRequest user;

    private Long departmentId;

    private String title;

    private Boolean isActive;
}