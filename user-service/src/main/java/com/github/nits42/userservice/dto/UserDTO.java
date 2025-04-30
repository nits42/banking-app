package com.github.nits42.userservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class UserDTO implements Serializable {
    private String id;
    private String username;
    private String password;
    private String email;
    private String role;
}
