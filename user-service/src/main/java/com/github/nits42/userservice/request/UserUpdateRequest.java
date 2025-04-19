package com.github.nits42.userservice.request;

import com.github.nits42.userservice.entities.UserDetails;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest implements Serializable {

    private String password;
    private UserDetails userDetailsRequest;

}
