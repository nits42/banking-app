package com.github.nits42.corebankingservice.request;


import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Username is required")
    @Size(min = 6, message = "Username should contains at-least 6 characters")
    private String username;

    @NotBlank(message = "Email is required")
    @Email
    private String email;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotNull(message = "Phone number is required")
    @Size(min = 10, max = 10, message = "Phone number should contain 10 digits")
    private Long phoneNumber;

    @NotBlank(message = "Date of birth is required")
    @Past
    private String dateOfBirth;

    private String aboutMe;

}
