package com.example.obs.business.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementCreateRequest {

    @NotBlank(message = "Duyuru başlığı boş olamaz")
    @Size(max = 255, message = "Duyuru başlığı 255 karakterden fazla olamaz")
    private String title;

    @NotBlank(message = "Duyuru içeriği boş olamaz")
    private String content;

    @NotNull(message = "Yayınlanma tarihi boş olamaz")
    private LocalDateTime publishedDate;

    private Boolean isActive = true;
}
