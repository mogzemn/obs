package com.example.obs.business.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentCreateRequest {

    @Valid
    @NotNull(message = "Kullanıcı bilgileri boş olamaz")
    private UserCreateRequest user;

    @NotNull(message = "Bölüm ID boş olamaz")
    private Long departmentId;

    private Long advisorId;
}
