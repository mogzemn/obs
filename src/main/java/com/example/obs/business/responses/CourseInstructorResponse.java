package com.example.obs.business.responses;

import com.example.obs.model.enums.Semester;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseInstructorResponse {
    private Long id;
    private CourseResponse course;
    private AcademicResponse academic;
    private Semester semester;
    private String academicYear;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}