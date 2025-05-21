package com.example.obs.business.requests;

import com.example.obs.model.enums.Semester;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentCourseCreateRequest {
    
    private Long studentId;
    private Long courseId;
    private Semester semester;
}
