package com.example.obs.business.requests;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GradeUpdateRequest {
    private Long id;

    @DecimalMin(value = "0.0", message = "Vize notu 0'dan küçük olamaz")
    @DecimalMax(value = "100.0", message = "Vize notu 100'den büyük olamaz")
    private BigDecimal midtermGrade;

    @DecimalMin(value = "0.0", message = "Ödev notu 0'dan küçük olamaz")
    @DecimalMax(value = "100.0", message = "Ödev notu 100'den büyük olamaz")
    private BigDecimal assignmentGrade;

    @DecimalMin(value = "0.0", message = "Final notu 0'dan küçük olamaz")
    @DecimalMax(value = "100.0", message = "Final notu 100'den büyük olamaz")
    private BigDecimal finalGrade;

    @DecimalMin(value = "0.0", message = "Bütünleme notu 0'dan küçük olamaz")
    @DecimalMax(value = "100.0", message = "Bütünleme notu 100'den büyük olamaz")
    private BigDecimal makeupGrade;

    @DecimalMin(value = "0.0", message = "Vize ağırlığı 0'dan küçük olamaz")
    @DecimalMax(value = "1.0", message = "Vize ağırlığı 1'den büyük olamaz")
    private BigDecimal midtermWeight;

    @DecimalMin(value = "0.0", message = "Ödev ağırlığı 0'dan küçük olamaz")
    @DecimalMax(value = "1.0", message = "Ödev ağırlığı 1'den büyük olamaz")
    private BigDecimal assignmentWeight;

    @DecimalMin(value = "0.0", message = "Final ağırlığı 0'dan küçük olamaz")
    @DecimalMax(value = "1.0", message = "Final ağırlığı 1'den büyük olamaz")
    private BigDecimal finalWeight;

    @Size(max = 2, message = "Harf notu 2 karakterden fazla olamaz")
    private String letterGrade;

    private Boolean isPassed;
}