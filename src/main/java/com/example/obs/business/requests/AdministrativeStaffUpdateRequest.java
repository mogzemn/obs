package com.example.obs.business.requests;

import com.example.obs.model.enums.AdministrativeUnit;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdministrativeStaffUpdateRequest {

    private Long id;
    @Valid
    private UserUpdateRequest user;

    @NotNull(message = "Birim adı boş olamaz")
    private AdministrativeUnit unitName;
}
