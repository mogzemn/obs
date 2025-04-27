package com.example.obs.business.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdministrativeStaffResponse {
    private Long id;
    private UserResponse user;
    private String staffNumber;
    private String jobTitle;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}