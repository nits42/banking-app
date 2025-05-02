package com.github.nits42.authservice.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.nits42.authservice.enums.Role;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequest implements Serializable {

    private String username;
    private String password;
    private String email;
    @JsonIgnore
    private Role role;

}
