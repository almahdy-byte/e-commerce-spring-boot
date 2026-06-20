package com.nazer.e_commerce.auth.Dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nazer.e_commerce.users.enums.UserRoles;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RigsterDto {
    @NotBlank(message = "first name is required")
    @Size(min = 3 , max = 15 , message = "the length must be in range from 3 to 15 character")
    private String firstName;

    @NotBlank(message = "last name is required")
    @Size(min = 3 , max = 15 , message = "the length must be in range from 3 to 15 character")
    private String lastName;

    @NotBlank(message = "email is required")
    @Email(message = "email must be in valid format user@example.com")
    private String email;

    @NotBlank(message = "last name is required")
    @Size(min = 6, message = "the password must be greater than or equal 6")
    private String password;

    @NotBlank(message = "phone number is required")
    private String phoneNumber;

    @Past(message = "date of birth must be in the past")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;

    @Builder.Default
    private UserRoles role = UserRoles.USER;
}
