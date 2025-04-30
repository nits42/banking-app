package com.github.nits42.userservice.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
/**
 * Data Transfer Object for User entity.
 * This class is used to transfer user data between different layers of the application.
 * It contains only the necessary fields for user registration and login.
 */
public class UserCreateRequest implements Serializable {

    @NotBlank(message = "Username is required")
    @Size(min = 6, message = "Username should contains at-least 6characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password should contains at-least 8 characters")
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    private String requestFrom;

}
