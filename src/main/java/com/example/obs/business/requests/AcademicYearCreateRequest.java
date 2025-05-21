package com.example.obs.business.requests;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcademicYearCreateRequest {

    @NotNull(message = "Başlangıç tarihi boş olamaz")
    @FutureOrPresent(message = "Başlangıç tarihi şu an veya gelecekte olmalıdır")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = "Bitiş tarihi boş olamaz")
    @Future(message = "Bitiş tarihi gelecekte olmalıdır")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

}
