package com.github.nits42.atmservice.service.impl;

import com.github.nits42.atmservice.client.CoreBankingServiceClient;
import com.github.nits42.atmservice.dto.TransactionDTO;
import com.github.nits42.atmservice.entities.Card;
import com.github.nits42.atmservice.exceptions.CardNotFoundException;
import com.github.nits42.atmservice.repository.CardRepository;
import com.github.nits42.atmservice.request.*;
import com.github.nits42.atmservice.service.ATMService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service("atmService")
@RequiredArgsConstructor
public class ATMServiceImpl implements ATMService {

    private final CoreBankingServiceClient coreBankingServiceClient;
    private final CardRepository cardRepository;
    private final ModelMapper modelMapper;

    @Override
    public String deposit(CardDepositRequest request) {
        Card card = cardRepository.findByCardNumberAndSecurityPin(request.getCardNo(), request.getPin())
                .orElseThrow(() ->
                        new CardNotFoundException("Card details not found"));

        AccountUpdateRequest req = AccountUpdateRequest.builder()
                .accountNo(card.getAccount().getAccountNumber())
                .amount(request.getAmount())
                .build();
        log.info("Calling Core-banking-service api");
        return coreBankingServiceClient.deposit(req).getBody();
    }

    @Override
    public String withdraw(CardWithdrawRequest request) {
        Card card = cardRepository.findByCardNumberAndSecurityPin(request.getCardNo(), request.getPin())
                .orElseThrow(() ->
                        new CardNotFoundException("Card details not found"));

        AccountUpdateRequest req = AccountUpdateRequest.builder()
                .accountNo(card.getAccount().getAccountNumber())
                .amount(request.getAmount())
                .build();
        log.info("Calling Core-banking-service api");
        return coreBankingServiceClient.withdraw(req).getBody();
    }

    @Override
    public String fundTransfer(FundTransferrRequest request) {
        Card card = cardRepository.findByCardNumberAndSecurityPin(request.getCardNo(), request.getPin())
                .orElseThrow(() ->
                        new CardNotFoundException("Card details not found"));

        FundTransferRequest req = FundTransferRequest.builder()
                .fromAccount(card.getAccount().getAccountNumber())
                .toAccount(request.getToAccount())
                .amount(request.getAmount())
                .build();
        log.info("Calling Core-banking-service api");
        return coreBankingServiceClient.fundTransfer(req).getBody();
    }

    @Override
    public Double checkBalance(CardDetailsRequest request) {
        Card card = cardRepository.findByCardNumberAndSecurityPin(request.getCardNo(), request.getPin())
                .orElseThrow(() ->
                        new CardNotFoundException("Card details not found"));
        log.info("Calling Core-banking-service api");
        return coreBankingServiceClient.checkBalance(card.getAccount().getAccountNumber()).getBody();
    }

    @Override
    public List<TransactionDTO> miniStatement(CardDetailsRequest request) {
        Card card = cardRepository.findByCardNumberAndSecurityPin(request.getCardNo(), request.getPin())
                .orElseThrow(() ->
                        new CardNotFoundException("Card details not found"));
        log.info("Calling Core-banking-service api");
        return coreBankingServiceClient.miniStatement(card.getAccount().getAccountNumber()).getBody();
    }

    @Override
    public String pinChange(CardPinChangeRequest request) {
        log.info("Calling Core-banking-service api");
        return coreBankingServiceClient.pinChange(request).getBody();
    }

}
