package com.example.obs.business.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse extends LoginResponse {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String role;

    public JwtResponse(String token, String type, String refreshToken,
                       Long id, String email, String firstName, String lastName, String role) {
        super(token, type, refreshToken);
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }
}