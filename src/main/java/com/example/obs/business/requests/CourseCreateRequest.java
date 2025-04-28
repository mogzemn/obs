package com.example.obs.business.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseCreateRequest {

    @NotBlank(message = "Ders kodu boş olamaz")
    @Size(min = 6,max = 6, message = "Ders kodu 6 karakterden oluşmalıdır")
    private String courseCode;

    @NotBlank(message = "Ders adı boş olamaz")
    @Size(max = 50, message = "Ders adı 50 karakterden fazla olamaz")
    private String courseName;

    @NotNull(message = "Kredi sayısı boş olamaz")
    @Min(value = 1, message = "Kredi sayısı en az 1 olmalıdır")
    private Integer credits;

    @NotNull(message = "ECTS değeri boş olamaz")
    @Min(value = 1, message = "ECTS değeri en az 1 olmalıdır")
    private Integer ects;

    @NotNull(message = "Bölüm ID boş olamaz")
    private Long departmentId;

    @NotNull(message = "Aktiflik durumu boş olamaz")
    private Boolean isActive;
}