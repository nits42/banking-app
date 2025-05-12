package com.github.nits42.atmservice.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class FundTransferRequest implements Serializable {

    @NotNull(message = "Source account number is required")
    private Long fromAccount;

    @NotNull(message = "Destination account number is required")
    private Long toAccount;

    @NotNull(message = "Amount is required")
    private double amount;

}
