package com.nazer.e_commerce.auth.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginDto {
    @NotBlank(message = "email is required")
    @Email(message = "emial nust be in a valid format user@example.com")
    private String email;

    @NotBlank(message = "password is required")
    private String password;
}
