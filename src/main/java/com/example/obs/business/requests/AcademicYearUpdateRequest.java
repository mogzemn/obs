package com.example.obs.business.requests;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcademicYearUpdateRequest {

    @NotNull(message = "Akademik Yıl ID boş olamaz")
    @Positive(message = "Akademik Yıl ID pozitif olmalıdır")
    private Long id;

    @NotNull(message = "Başlangıç tarihi boş olamaz")
    @FutureOrPresent(message = "Başlangıç tarihi şu an veya gelecekte olmalıdır")
    private LocalDate startDate;

    @NotNull(message = "Bitiş tarihi boş olamaz")
    @Future(message = "Bitiş tarihi gelecekte olmalıdır")
    private LocalDate endDate;

}
