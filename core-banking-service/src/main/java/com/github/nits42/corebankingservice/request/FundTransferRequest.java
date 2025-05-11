package com.github.nits42.corebankingservice.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data

public class FundTransferRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Source account number is required")
    private Long fromAccount;

    @NotNull(message = "Destination account number is required")
    private Long toAccount;

    @NotNull(message = "Amount is required")
    private double amount;

}
