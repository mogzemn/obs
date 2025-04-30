package com.example.obs.business.responses;

import com.example.obs.model.entity.Department;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcademicResponse {
    private int id;
    private UserResponse user;
    private Department department;
    private String registrationNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}