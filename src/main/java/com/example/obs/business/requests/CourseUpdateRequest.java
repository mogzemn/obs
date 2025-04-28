package com.example.obs.business.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseUpdateRequest {
    @NotNull(message = "Ders ID'si boş olamaz")
    private Long id;

    @Size(max = 100, message = "Ders adı 100 karakterden fazla olamaz")
    private String courseName;

    @Size(min = 6, max = 6, message = "Ders kodu tam olarak 6 karakter olmalıdır")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "Ders kodu büyük harf ve rakamlardan oluşmalıdır")
    private String courseCode;

    private Long departmentId;

    @Min(value = 1, message = "Kredi değeri en az 1 olmalıdır")
    private Integer credits;

    @Min(value = 1, message = "ECTS değeri en az 1 olmalıdır")
    private Integer ects;

    private String description;

    private Boolean isActive;
}