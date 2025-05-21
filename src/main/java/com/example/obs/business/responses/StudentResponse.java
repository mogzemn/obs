package com.example.obs.business.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponse {
    private Long id;
    private UserResponse user;
    private DepartmentResponse department;
    private AcademicResponse advisor;
    private String studentNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
