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
public class GradeResponse {
    private Long id;
    private StudentResponse student;
    private CourseResponse course;
    private AcademicResponse academic;
    private Semester semester;
    private String academicYear;
    private BigDecimal midtermGrade;
    private BigDecimal assignmentGrade;
    private BigDecimal finalGrade;
    private BigDecimal makeupGrade;
    private BigDecimal midtermWeight;
    private BigDecimal assignmentWeight;
    private BigDecimal finalWeight;
    private BigDecimal average;
    private String letterGrade;
    private Boolean isPassed;
    private UserResponse createdBy;
    private LocalDateTime createdDate;
    private UserResponse updatedBy;
    private LocalDateTime updatedDate;
}