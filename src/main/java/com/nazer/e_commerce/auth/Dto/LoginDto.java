package com.nazer.e_commerce.auth.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    @NotBlank(message = "email is required")
    @Email(message = "emial nust be in a valid format user@example.com")
    private String email;

    @NotBlank(message = "password is required")
    private String password;
}
