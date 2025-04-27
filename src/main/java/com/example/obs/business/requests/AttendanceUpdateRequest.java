package com.example.obs.business.requests;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceUpdateRequest {

    @DecimalMin(value = "0.0", message = "Devamsızlık süresi 0'dan küçük olamaz")
    private BigDecimal absenceDuration;

    @Size(max = 255, message = "Notlar 255 karakterden fazla olamaz")
    private String notes;
}