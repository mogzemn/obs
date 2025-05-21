package com.example.obs.business.requests;

import com.example.obs.model.enums.Semester;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentCourseUpdateRequest {
    
    private Long id;
    private Long studentId;
    private Long courseId;
    private Semester semester;
    private Boolean isActive;
    private Boolean isCompleted;
    private Boolean isPassed;
}
