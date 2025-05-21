package com.example.obs.business.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceUpdateRequest {
    
    @NotNull(message = "ID boş olamaz")
    private Long id;
    
    @NotNull(message = "Devamsızlık süresi boş olamaz")
    @Min(value = 0, message = "Devamsızlık süresi 0'dan küçük olamaz")
    private Integer absenceDuration;
    
    @Size(max = 255, message = "Notlar 255 karakterden fazla olamaz")
    private String notes;
}