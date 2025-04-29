package com.example.obs.business.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseInstructorUpdateRequest {

    private Long id;

    private Long academicId;

    private Boolean isActive;
}