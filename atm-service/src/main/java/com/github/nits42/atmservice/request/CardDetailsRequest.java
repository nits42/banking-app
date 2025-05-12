package com.github.nits42.atmservice.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class CardDetailsRequest implements Serializable {

    @NotNull(message = "ATM card number is required")
    private Long cardNo;

    @NotNull(message = "ATM card pin is required")
    private Integer pin;

}
