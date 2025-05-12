package com.github.nits42.atmservice.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class AccountUpdateRequest implements Serializable {

    @NotNull(message = "accountNo is required")
    private Long accountNo;

    @NotNull(message = "amount is required")
    private double amount;

}
