package com.example.obs.business.responses;

import com.example.obs.model.enums.AdministrativeUnit;
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
    private AdministrativeUnit unitName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
