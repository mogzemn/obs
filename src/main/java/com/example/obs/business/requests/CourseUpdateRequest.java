package com.example.obs.business.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;
import java.math.BigDecimal;
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

    @Size(min = 7, max = 7, message = "Ders kodu tam olarak 7 karakter olmalıdır")
    @Pattern(regexp = "^[A-ZÇĞİÖŞÜ]{3}\\s\\d{3}$", message = "Ders kodu formatı 'BİL 101' şeklinde olmalıdır (3 büyük harf + boşluk + 3 rakam)")
    private String courseCode;

    private Long departmentId;

    @Min(value = 1, message = "Kredi değeri en az 1 olmalıdır")
    private Integer credits;

    @Min(value = 1, message = "ECTS değeri en az 1 olmalıdır")
    private Integer ects;

    @Size(max = 1000, message = "Ders açıklaması 1000 karakterden fazla olamaz")
    private String description;

    @Size(max = 50, message = "Derslik 50 karakterden fazla olamaz")
    private String classroom;

    private Boolean isActive;

    @DecimalMin(value = "0.0", message = "Vize ağırlığı 0'dan küçük olamaz")
    @DecimalMax(value = "100.0", message = "Vize ağırlığı 100'den büyük olamaz")
    private BigDecimal midtermWeight;

    @DecimalMin(value = "0.0", message = "Ödev ağırlığı 0'dan küçük olamaz")
    @DecimalMax(value = "100.0", message = "Ödev ağırlığı 100'den büyük olamaz")
    private BigDecimal assignmentWeight;

    @DecimalMin(value = "0.0", message = "Final ağırlığı 0'dan küçük olamaz")
    @DecimalMax(value = "100.0", message = "Final ağırlığı 100'den büyük olamaz")
    private BigDecimal finalWeight;
}
