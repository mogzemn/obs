package com.example.obs.business.responses;

import com.example.obs.model.enums.Role;
import com.example.obs.model.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String identityNumber;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    private String phone;
    private String email;
    private UserStatus status;
    private Boolean isActive;
    private Boolean hasLoginPermission;
    private Role role;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
