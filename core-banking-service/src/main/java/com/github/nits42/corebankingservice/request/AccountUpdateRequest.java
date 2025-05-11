package com.github.nits42.corebankingservice.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class AccountUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "accountNo is required")
    private Long accountNo;

    @NotNull(message = "amount is required")
    private double amount;

}
