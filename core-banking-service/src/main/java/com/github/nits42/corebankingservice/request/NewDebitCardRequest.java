package com.github.nits42.corebankingservice.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewDebitCardRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Account number is required")
    private long accountNumber;

    private String cardHolderName;

}
