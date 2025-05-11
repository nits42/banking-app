package com.github.nits42.corebankingservice.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class CardPinChangeRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "ATM card number is required")
    private Long cardNo;

    @NotNull(message = "Current pin is required ATM card number")
    private Integer pin;

    @NotNull(message = "New pin is required ATM card number")
    private Integer newPin;

}
