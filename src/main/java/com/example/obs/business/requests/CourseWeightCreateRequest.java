package com.example.obs.business.requests;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseWeightCreateRequest {

    @NotNull(message = "Ders ID boş olamaz")
    private Long courseId;

    @NotNull(message = "Vize ağırlığı boş olamaz")
    @DecimalMin(value = "0.0", message = "Vize ağırlığı 0'dan küçük olamaz")
    @DecimalMax(value = "100.0", message = "Vize ağırlığı 100'den büyük olamaz")
    private BigDecimal midtermWeight;

    @NotNull(message = "Final ağırlığı boş olamaz")
    @DecimalMin(value = "0.0", message = "Final ağırlığı 0'dan küçük olamaz")
    @DecimalMax(value = "100.0", message = "Final ağırlığı 100'den büyük olamaz")
    private BigDecimal finalWeight;

    @NotNull(message = "Ödev ağırlığı boş olamaz")
    @DecimalMin(value = "0.0", message = "Ödev ağırlığı 0'dan küçük olamaz")
    @DecimalMax(value = "100.0", message = "Ödev ağırlığı 100'den büyük olamaz")
    private BigDecimal assignmentWeight;
}
