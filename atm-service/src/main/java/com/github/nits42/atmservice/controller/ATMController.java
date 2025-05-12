package com.github.nits42.atmservice.controller;

import com.github.nits42.atmservice.dto.TransactionDTO;
import com.github.nits42.atmservice.request.*;
import com.github.nits42.atmservice.service.ATMService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/atm")
@RequiredArgsConstructor
public class ATMController {

    private final ATMService atmService;

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@Valid @RequestBody CardDepositRequest request) {
        return new ResponseEntity<>(atmService.deposit(request), HttpStatus.OK);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@Valid @RequestBody CardWithdrawRequest request) {
        return new ResponseEntity<>(atmService.withdraw(request), HttpStatus.OK);
    }

    @PostMapping("/fundTransfer")
    public ResponseEntity<String> fundTransfer(@Valid @RequestBody FundTransferrRequest request) {
        return new ResponseEntity<>(atmService.fundTransfer(request), HttpStatus.OK);
    }

    @PostMapping("/checkBalance")
    public ResponseEntity<Double> checkBalance(@Valid @RequestBody CardDetailsRequest request) {
        return new ResponseEntity<>(atmService.checkBalance(request), HttpStatus.OK);
    }

    @PostMapping("/miniStatement")
    public ResponseEntity<List<TransactionDTO>> miniStatement(@Valid @RequestBody CardDetailsRequest request) {
        return new ResponseEntity<>(atmService.miniStatement(request), HttpStatus.OK);
    }

    @PostMapping("/pinChange")
    public ResponseEntity<String> pinChange(@Valid @RequestBody CardPinChangeRequest request) {
        return new ResponseEntity<>(atmService.pinChange(request), HttpStatus.OK);
    }

}
