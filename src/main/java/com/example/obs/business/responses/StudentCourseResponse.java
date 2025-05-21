package com.example.obs.business.responses;

import com.example.obs.model.enums.Semester;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentCourseResponse {
    
    private Long id;
    private Long studentId;
    private String studentName;
    private String studentNumber;
    private Long courseId;
    private String courseName;
    private String courseCode;
    private Semester semester;
    private LocalDateTime registrationDate;
    private Boolean isActive;
    private Boolean isCompleted;
    private Boolean isPassed;
}
