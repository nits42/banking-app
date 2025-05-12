package com.github.nits42.atmservice.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class CardWithdrawRequest implements Serializable {

    @NotNull(message = "ATM Card number is required")
    private long cardNo;

    @NotNull(message = "ATM card pin is required")
    private int pin;

    @NotNull(message = "Amount is required")
    private double amount;

}
