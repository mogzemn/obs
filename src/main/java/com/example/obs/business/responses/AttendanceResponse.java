package com.example.obs.business.responses;

import com.example.obs.model.enums.Semester;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceResponse {
    private Long id;
    private CourseResponse course;
    private StudentResponse student;
    private AcademicResponse academic;
    private Semester semester;
    private String academicYear;
    private BigDecimal absenceDuration;
    private String notes;
    private UserResponse createdBy;
    private LocalDateTime createdDate;
    private UserResponse updatedBy;
    private LocalDateTime updatedDate;
}