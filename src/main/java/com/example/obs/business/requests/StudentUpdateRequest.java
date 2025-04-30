package com.example.obs.business.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentUpdateRequest {
    @NotNull(message = "Öğrenci ID'si boş olamaz")
    private int id;

    @Valid
    private UserUpdateRequest user;

    private int departmentId;

    private int advisorId;
}