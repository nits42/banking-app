package com.github.nits42.userservice.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String token;

    private String username;

}