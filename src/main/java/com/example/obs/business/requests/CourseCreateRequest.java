package com.example.obs.business.requests;

import jakarta.validation.constraints.NotBlank;
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
public class CourseCreateRequest {
    @NotBlank(message = "Ders adı boş olamaz")
    @Size(max = 100, message = "Ders adı 100 karakterden fazla olamaz")
    private String courseName;

    @NotBlank(message = "Ders kodu boş olamaz")
    @Size(min = 6, max = 6, message = "Ders kodu tam olarak 6 karakter olmalıdır")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "Ders kodu büyük harf ve rakamlardan oluşmalıdır")
    private String courseCode;

    @NotNull(message = "Bölüm ID boş olamaz")
    private Long departmentId;

    @NotNull(message = "Kredi değeri boş olamaz")
    @Min(value = 1, message = "Kredi değeri en az 1 olmalıdır")
    private Integer credits;

    @NotNull(message = "ECTS değeri boş olamaz")
    @Min(value = 1, message = "ECTS değeri en az 1 olmalıdır")
    private Integer ects;

    private String description;

    @NotNull(message = "Aktiflik durumu boş olamaz")
    private Boolean isActive;
}