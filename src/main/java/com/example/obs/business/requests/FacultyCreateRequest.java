package com.example.obs.business.requests;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacultyCreateRequest {

    private String facultyName;

    private String facultyCode;

    private Boolean isActive;

}
