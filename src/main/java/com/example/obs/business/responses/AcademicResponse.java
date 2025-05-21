package com.example.obs.business.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcademicResponse {
    private Long id;
    private UserResponse user;
    private DepartmentResponse department;
    private String academicNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}