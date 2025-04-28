package com.example.obs.business.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseUpdateRequest {

    @Size(min = 6, max = 6, message = "Ders kodu 6 karakterden oluşmalıdır")
    private String courseCode;

    @Size(max = 50, message = "Ders adı 50 karakterden fazla olamaz")
    private String courseName;

    @Min(value = 1, message = "Kredi sayısı en az 1 olmalıdır")
    private Integer credits;

    @Min(value = 1, message = "ECTS değeri en az 1 olmalıdır")
    private Integer ects;

    private Long departmentId;

    private Boolean isActive;
}