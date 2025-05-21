package com.example.obs.business.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.obs.model.enums.ExamType;
import com.example.obs.model.enums.Semester;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamUpdateRequest {
    @NotNull(message = "ID boş olamaz")
    private Long id;
    
    @NotNull(message = "Ders ID boş olamaz")
    private Long courseId;

    @NotNull(message = "Sınav tipi boş olamaz")
    private ExamType examType;

    @NotNull(message = "Dönem bilgisi boş olamaz")
    private Semester semester;

    @NotNull(message = "Akademik Yıl ID boş olamaz")
    private Long academicYearId;

    @NotNull(message = "Sınav başlangıç tarihi boş olamaz")
    @FutureOrPresent(message = "Sınav başlangıç tarihi bugün veya gelecekte olmalıdır")
    private LocalDateTime examDate;

    @NotNull(message = "Sınav bitiş tarihi boş olamaz")
    @FutureOrPresent(message = "Sınav bitiş tarihi bugün veya gelecekte olmalıdır")
    private LocalDateTime examEndDate;

    @Size(max = 100, message = "Sınav lokasyonu 100 karakterden fazla olamaz")
    private String location;

    @Size(max = 500, message = "Açıklama 500 karakterden fazla olamaz")
    private String description;

    @NotNull(message = "Aktiflik durumu boş olamaz")
    private Boolean isActive;

    private Long updatedById;
}
