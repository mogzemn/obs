package com.example.obs.business.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TranscriptResponse {
    private Long id;
    private StudentResponse student;
    private Double gpa;
    private LocalDate transcriptDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
