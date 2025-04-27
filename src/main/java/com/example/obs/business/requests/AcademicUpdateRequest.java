package com.example.obs.business.requests;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcademicUpdateRequest {

    @Valid
    private UserUpdateRequest user;

    private Long departmentId;
}