package com.example.obs.business.requests;

import com.example.obs.model.enums.Semester;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseInstructorCreateRequest {

    @NotNull(message = "Ders ID boş olamaz")
    private Long courseId;

    @NotNull(message = "Akademisyen ID boş olamaz")
    private Long academicId;

    @NotNull(message = "Dönem bilgisi boş olamaz")
    private Semester semester;

    @NotNull(message = "Akademik yıl ID boş olamaz")
    @Positive(message = "Akademik yıl ID pozitif olmalıdır")
    private Long academicYearId;

    @NotNull(message = "Aktiflik durumu boş olamaz")
    private Boolean isActive;
}
