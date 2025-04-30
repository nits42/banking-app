package com.github.nits42.authservice.request;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest implements Serializable {

    private String requestFrom;
    private String username;
    private String password;
    private String email;

}
