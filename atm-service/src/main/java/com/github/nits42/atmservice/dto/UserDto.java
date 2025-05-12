package com.github.nits42.atmservice.dto;

import com.github.nits42.atmservice.enums.Role;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class UserDto implements Serializable {
    private UUID id;
    private String username;
    private String password;
    private Role role;
}
