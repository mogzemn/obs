package com.example.obs.business.requests;

import com.example.obs.model.enums.Semester;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
 
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceCreateRequest {

    @NotNull(message = "Ders ID boş olamaz")
    private Long courseId;

    @NotNull(message = "Öğrenci ID boş olamaz")
    private Long studentId;

    @NotNull(message = "Akademisyen ID boş olamaz")
    private Long academicId;

    @NotNull(message = "Dönem bilgisi boş olamaz")
    private Semester semester;

    @NotNull(message = "Akademik yıl ID boş olamaz")
    @Positive(message = "Akademik yıl ID pozitif olmalıdır") 
    private Long academicYearId;

    @NotNull(message = "Devamsızlık süresi boş olamaz")
    @Min(value = 0, message = "Devamsızlık süresi 0'dan küçük olamaz")
    private Integer absenceDuration;

    @Size(max = 255, message = "Notlar 255 karakterden fazla olamaz")
    private String notes;
}