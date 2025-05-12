package com.github.nits42.atmservice.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class FundTransferrRequest implements Serializable {

    @NotNull(message = "ATM card number is required")
    private Long cardNo;

    @NotNull(message = "ATM card pin is required")
    private Integer pin;

    @NotNull(message = "Destination account number is required")
    private Long toAccount;

    @NotNull(message = "Amount is required")
    private double amount;

}
