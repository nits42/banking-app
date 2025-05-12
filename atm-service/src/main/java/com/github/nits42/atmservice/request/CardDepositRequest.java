package com.github.nits42.atmservice.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class CardDepositRequest implements Serializable {

    @NotNull(message = "ATM Card number is required")
    private Long cardNo;

    @NotNull(message = "ATM card pin is required")
    private Integer pin;

    @NotNull(message = "Amount is required")
    private double amount;

}
