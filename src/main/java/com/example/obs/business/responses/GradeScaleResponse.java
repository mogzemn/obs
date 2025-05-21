package com.example.obs.business.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GradeScaleResponse {
    
    private Long id;
    private Long academicId;
    private String academicName;
    private Long courseId;
    private String courseName;
    private String name;
    private Boolean isActive;
    private Boolean isDefault;
    
    private Double aaMin;
    private Double baMin;
    private Double bbMin;
    private Double cbMin;
    private Double ccMin;
    private Double dcMin;
    private Double ddMin;
    private Double fdMin;
    private Double ffMin;
    
    private Long createdById;
    private String createdByName;
    private LocalDateTime createdAt;
    private Long updatedById;
    private String updatedByName;
    private LocalDateTime updatedAt;
}
