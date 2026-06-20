package com.nazer.e_commerce.users.Dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileDto {
    @Size(min = 3, max = 15, message = "the length must be in range from 3 to 15 character")
    private String firstName;

    @Size(min = 3, max = 15, message = "the length must be in range from 3 to 15 character")
    private String lastName;

    private String phoneNumber;

    @Past(message = "date of birth must be in the past")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;
}
