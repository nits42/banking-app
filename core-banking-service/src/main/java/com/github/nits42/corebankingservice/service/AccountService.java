package com.github.nits42.corebankingservice.service;

import com.github.nits42.corebankingservice.dto.AccountDTO;
import com.github.nits42.corebankingservice.dto.TransactionDTO;
import com.github.nits42.corebankingservice.request.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Set;

public interface AccountService {

    String createAccount(AccountRequest request);

    String deposit(AccountUpdateRequest request);

    String withdraw(AccountUpdateRequest request);

    Set<AccountDTO> getAllAccountByUsername(String username);

    Set<AccountDTO> getAllAccountByUserId(String id);

    String deleteAccount(Long accountNo);

    AccountDTO getAccountByAccountNo(Long accountNo);

    Long getCountAllAccount();

    Long getCountAllActiveAccount();

    Long getCountAllInactiveAccount();

    String fundTransfer(FundTransferRequest request);

    String openAccount(NewAccountRequest request);

    Double checkingBalance(Long accountNo);

    List<TransactionDTO> miniStatement(Long accountNo);

    List<TransactionDTO> detailedStatement(Long accountNo, String startDate, String endDate);

    String pinChange(@Valid CardPinChangeRequest request);

    String addNewDebitCard(@Valid NewDebitCardRequest request);

    String blockDebitOrCreditCard(BlockDebitOrCreditCardRequest request);

}
