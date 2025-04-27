package com.example.obs.business.responses;

import com.example.obs.model.entity.Academic;
import com.example.obs.model.entity.Department;
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
    private Department department;
    private Academic advisor;
    private String studentNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
