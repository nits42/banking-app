package com.github.nits42.authservice.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotEmpty(message = "Email cannot be empty")
    private String username;

    @NotEmpty(message = "Password cannot be empty")
    private String password;
    
}
