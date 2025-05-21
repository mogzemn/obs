package com.example.obs.business.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TranscriptCreateRequest {

    @NotNull(message = "Öğrenci ID boş olamaz")
    private Long studentId;

    @NotNull(message = "Transkript tarihi boş olamaz")
    @PastOrPresent(message = "Transkript tarihi geçmiş veya bugün olmalıdır")
    private LocalDate transcriptDate;
}
