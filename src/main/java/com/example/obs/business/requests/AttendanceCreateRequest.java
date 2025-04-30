package com.example.obs.business.requests;

import com.example.obs.model.enums.Semester;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceCreateRequest {

    @NotNull(message = "Ders ID boş olamaz")
    private int courseId;

    @NotNull(message = "Öğrenci ID boş olamaz")
    private int studentId;

    @NotNull(message = "Akademisyen ID boş olamaz")
    private int academicId;

    @NotNull(message = "Dönem bilgisi boş olamaz")
    private Semester semester;

    @NotBlank(message = "Akademik yıl boş olamaz")
    @Size(max = 20, message = "Akademik yıl 20 karakterden fazla olamaz")
    private String academicYear;

    @NotNull(message = "Devamsızlık süresi boş olamaz")
    @DecimalMin(value = "0.0", message = "Devamsızlık süresi 0'dan küçük olamaz")
    private BigDecimal absenceDuration;

    @Size(max = 255, message = "Notlar 255 karakterden fazla olamaz")
    private String notes;
}