package com.github.nits42.userservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.nits42.userservice.enums.Role;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

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
    private UUID id;
    private String username;
    private String password;
    private String email;
    private Role role;
    private UserDetailsDTO userDetails;

}
