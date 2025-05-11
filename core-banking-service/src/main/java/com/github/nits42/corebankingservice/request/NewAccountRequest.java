package com.github.nits42.corebankingservice.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class NewAccountRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private UserRequest user;
    private NewAddressRequest address;
    private AccounttRequest account;

}
