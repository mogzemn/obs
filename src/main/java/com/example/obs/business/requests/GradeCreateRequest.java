package com.example.obs.business.requests;

import com.example.obs.model.enums.Semester;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GradeCreateRequest {

    @NotNull(message = "Öğrenci ID boş olamaz")
    private Long studentId;

    @NotNull(message = "Ders ID boş olamaz")
    private Long courseId;

    @NotNull(message = "Akademisyen ID boş olamaz")
    private Long academicId;

    @NotNull(message = "Dönem bilgisi boş olamaz")
    private Semester semester;

    @NotNull(message = "Akademik yıl boş olamaz")
    @Size(max = 20, message = "Akademik yıl 20 karakterden fazla olamaz")
    private String academicYear;

    @DecimalMin(value = "0.0", message = "Vize notu 0'dan küçük olamaz")
    @DecimalMax(value = "100.0", message = "Vize notu 100'den büyük olamaz")
    private BigDecimal midtermGrade;

    @DecimalMin(value = "0.0", message = "Ödev notu 0'dan küçük olamaz")
    @DecimalMax(value = "100.0", message = "Ödev notu 100'den büyük olamaz")
    private BigDecimal assignmentGrade;

    @DecimalMin(value = "0.0", message = "Final notu 0'dan kücük olamaz")
    @DecimalMax(value = "100.0", message = "Final notu 100'den büyük olamaz")
    private BigDecimal finalGrade;

    @DecimalMin(value = "0.0", message = "Bütünleme notu 0'dan küçük olamaz")
    @DecimalMax(value = "100.0", message = "Bütünleme notu 100'den büyük olamaz")
    private BigDecimal makeupGrade;

    @NotNull(message = "Vize ağırlığı boş olamaz")
    @DecimalMin(value = "0.0", message = "Vize ağırlığı 0'dan küçük olamaz")
    @DecimalMax(value = "1.0", message = "Vize ağırlığı 1'den büyük olamaz")
    private BigDecimal midtermWeight;

    @NotNull(message = "Ödev ağırlığı boş olamaz")
    @DecimalMin(value = "0.0", message = "Ödev ağırlığı 0'dan küçük olamaz")
    @DecimalMax(value = "1.0", message = "Ödev ağırlığı 1'den büyük olamaz")
    private BigDecimal assignmentWeight;

    @NotNull(message = "Final ağırlığı boş olamaz")
    @DecimalMin(value = "0.0", message = "Final ağırlığı 0'dan küçük olamaz")
    @DecimalMax(value = "1.0", message = "Final ağırlığı 1'den büyük olamaz")
    private BigDecimal finalWeight;
}