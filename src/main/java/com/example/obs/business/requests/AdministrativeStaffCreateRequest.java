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
public class AdministrativeStaffCreateRequest {

    @Valid
    @NotNull(message = "Kullanıcı bilgileri boş olamaz")
    private UserCreateRequest user;

    @NotNull(message = "Birim adı boş olamaz")
    private AdministrativeUnit unitName;
}
