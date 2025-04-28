package com.example.obs.business.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentResponse {
    private Long id;
    private String departmentName;
    private String departmentCode;
    private FacultyResponse faculty;
    private AcademicResponse headOfDepartment;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}