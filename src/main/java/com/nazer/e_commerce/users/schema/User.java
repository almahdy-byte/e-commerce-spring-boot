package com.nazer.e_commerce.users.schema;



import java.time.LocalDate;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nazer.e_commerce.users.enums.Provider;
import com.nazer.e_commerce.users.enums.UserRoles;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.*;


@Document(collection = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    @Id
    @Getter(AccessLevel.NONE)
    private ObjectId id;

    @JsonIgnore
    public ObjectId getId() {
        return id;
    }

    @JsonProperty("_id")
    public String get_id() {
        return id != null ? id.toHexString() : null;
    }

    @NotBlank(message = "first name is required")
    private String firstName;

    @NotBlank(message = "first name is required")
    private String lastName;

    
    private String fullName;

    @NotBlank(message = "email is required")
    @Email(message = "email must be a valid format => user@exaple.com")
    private String email;

    @JsonIgnore
    private String password;


    @Builder.Default
    private UserRoles role = UserRoles.USER;

    @Builder.Default
    private Provider provider = Provider.SYSTEM;

    @Builder.Default
    private boolean isDeleted = false;
    
    @NotBlank(message = "phone number is required")
    private String phoneNumber;

    @Past(message = "date of birth must be in the past")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;
    
}
