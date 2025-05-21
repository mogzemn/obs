package com.example.obs.business.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseWeightResponse {
    private Long id;
    private Long courseId;
    private String courseName;
    private BigDecimal midtermWeight;
    private BigDecimal finalWeight;
    private BigDecimal assignmentWeight;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long createdById;
    private Long updatedById;
}
