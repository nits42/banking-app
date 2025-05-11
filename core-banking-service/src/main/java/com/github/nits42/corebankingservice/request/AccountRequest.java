package com.github.nits42.corebankingservice.request;

import com.github.nits42.corebankingservice.entities.Address;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
public class AccountRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Username is required")
    @Size(min = 6, message = "Username must be at least 6 characters")
    private String username;

    @NotBlank(message = "Address is required")
    private Address address;

    @NotBlank(message = "Account Type is required")
    private String accountType;

    @NotNull(message = "Opening balance is required")
    private double balance;

}
