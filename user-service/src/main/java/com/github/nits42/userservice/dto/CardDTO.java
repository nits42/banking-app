package com.github.nits42.userservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long cardNumber;
    private String cardHolderName;
    private String provider;
    private LocalDate cardExpiry;
    private int cvv;
    private String cardType;


}
