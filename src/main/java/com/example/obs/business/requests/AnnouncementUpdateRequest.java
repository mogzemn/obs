package com.example.obs.business.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementUpdateRequest {

    @NotNull(message = "Duyuru ID boş olamaz")
    private Long id;

    @Size(max = 255, message = "Duyuru başlığı 255 karakterden fazla olamaz")
    private String title;

    private String content;

    private LocalDateTime publishedDate;

    private Boolean isActive;
}
