package com.github.nits42.corebankingservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID transactionId;
    private String transactionType;
    private LocalDateTime transactionDate;
    private double amount;

}
