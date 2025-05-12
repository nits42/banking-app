package com.github.nits42.atmservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.nits42.atmservice.enums.Role;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthUserDTO implements Serializable {

    private UUID id;
    private String username;
    private String password;
    private Role role;

}