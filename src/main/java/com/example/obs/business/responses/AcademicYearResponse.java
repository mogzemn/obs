package com.example.obs.business.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.obs.model.enums.Semester; 

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcademicYearResponse {
    private Long id;

    private LocalDate startDate;
    private LocalDate endDate;
    private String name;
    private List<Semester> semesters; 
}
