package com.example.obs.business.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdministrativeStaffUpdateRequest {

    @Valid
    private UserUpdateRequest user;

    @Size(max = 255, message = "İş unvanı 255 karakterden fazla olamaz")
    private String jobTitle;
}