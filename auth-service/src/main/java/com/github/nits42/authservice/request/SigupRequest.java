package com.github.nits42.authservice.request;

import com.github.nits42.authservice.enums.Role;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SigupRequest implements Serializable {

    private String username;
    private String password;
    private String email;
    private Role role;

}
