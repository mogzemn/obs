package com.example.obs.business.responses;

import com.example.obs.model.entity.Department;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponse {
    private int id;
    private String courseCode;
    private String courseName;
    private Integer credits;
    private Integer ects;
    private Department department;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}