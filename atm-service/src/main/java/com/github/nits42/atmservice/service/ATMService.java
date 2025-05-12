package com.github.nits42.atmservice.service;

import com.github.nits42.atmservice.dto.TransactionDTO;
import com.github.nits42.atmservice.request.*;
import jakarta.validation.Valid;

import java.util.List;

public interface ATMService {

    String deposit(@Valid CardDepositRequest request);

    String withdraw(@Valid CardWithdrawRequest request);

    String fundTransfer(@Valid FundTransferrRequest request);

    Double checkBalance(@Valid CardDetailsRequest request);

    List<TransactionDTO> miniStatement(@Valid CardDetailsRequest request);

    String pinChange(CardPinChangeRequest request);

}
