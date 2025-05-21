package com.example.obs.business.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponse {
    private Long id;
    private String courseCode;
    private String courseName;
    private Integer credits;
    private Integer ects;
    private DepartmentResponse department;
    private String description;
    private String classroom;
    private Boolean isActive;
    private BigDecimal midtermWeight;
    private BigDecimal assignmentWeight;
    private BigDecimal finalWeight;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
