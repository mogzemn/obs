package com.example.obs.business.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "Personel numarası boş olamaz")
    @Size(max = 20, message = "Personel numarası 20 karakterden fazla olamaz")
    private String staffNumber;

    @Size(max = 255, message = "İş unvanı 255 karakterden fazla olamaz")
    private String jobTitle;
}
