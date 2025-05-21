package com.example.obs.business.responses;

import com.example.obs.model.entity.Course;
import com.example.obs.model.entity.User;
import com.example.obs.model.enums.ExamType;
import com.example.obs.model.enums.Semester;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamResponse {
    private Long id;
    private Course course;
    private ExamType examType;
    private Semester semester;
    private String academicYearName;
    private LocalDateTime examDate;
    private LocalDateTime examEndDate;
    private String location;
    private String description;
    private Boolean isActive;
    private User createdBy;
    private LocalDateTime createdAt;
    private User updatedBy;
    private LocalDateTime updatedAt;
}
