package com.example.obs.business.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TranscriptUpdateRequest {

    @NotNull(message = "Transkript ID boş olamaz")
    private Long id;

    @PositiveOrZero(message = "GANO pozitif veya sıfır olmalıdır")
    private Double gpa;

}
