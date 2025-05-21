package com.example.obs.business.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GradeScaleUpdateRequest {
    
    private Long id;
    private Long academicId;
    private Long courseId;
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
    private Double ffMin;
    
    private Long updatedById;
}
