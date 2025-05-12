package com.github.nits42.atmservice.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class CardPinChangeRequest implements Serializable {

    @NotNull(message = "ATM card number is required")
    private Long cardNo;

    @NotNull(message = "ATM card old pin is required")
    private Integer pin;

    @NotNull(message = "ATM card new pin is required")
    private Integer newPin;

}
