package com.github.nits42.corebankingservice.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class AccounttRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Account Type is required")
    private String accountType;

    @NotNull(message = "Opening balance is required")
    private double openingBalance;

}
