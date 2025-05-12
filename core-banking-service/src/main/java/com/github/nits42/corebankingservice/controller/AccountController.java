package com.github.nits42.corebankingservice.controller;

import com.github.nits42.corebankingservice.dto.AccountDTO;
import com.github.nits42.corebankingservice.dto.TransactionDTO;
import com.github.nits42.corebankingservice.request.*;
import com.github.nits42.corebankingservice.service.AccountService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/v1/account")
@RequiredArgsConstructor
@Tag(name = "Account", description = "Account API")
public class AccountController {

    private final AccountService accountService;

    @Hidden
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createAccount(@Valid @RequestBody AccountRequest request) {
        log.info("Account creating process is started for: {}", request.getUsername());
        return new ResponseEntity<>(accountService.createAccount(request), HttpStatus.CREATED);
    }


    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@Valid @RequestBody AccountUpdateRequest request) {
        log.info("Deposit process is started for: {}", request.getAccountNo());
        return new ResponseEntity<>(accountService.deposit(request), HttpStatus.OK);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@Valid @RequestBody AccountUpdateRequest request) {
        log.info("Withdrawal process is started for: {}", request.getAccountNo());
        return new ResponseEntity<>(accountService.withdraw(request), HttpStatus.OK);
    }


    @DeleteMapping("/{accountNo}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteAccount(@PathVariable Long accountNo) {
        log.info("Account deletion process is started for: {}", accountNo);
        return new ResponseEntity<>(accountService.deleteAccount(accountNo), HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<Set<AccountDTO>> getAllAccount() {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        log.info("Logged user: {}", username);
        return new ResponseEntity<>(accountService.getAllAccountByUsername(username), HttpStatus.OK);
    }

    @GetMapping("/{accountNo}")
    public ResponseEntity<AccountDTO> getAccountByAccountNo(@PathVariable Long accountNo) {
        log.info("Fetching account details based on account no.: {}", accountNo);
        return new ResponseEntity<>(accountService.getAccountByAccountNo(accountNo), HttpStatus.OK);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<Set<AccountDTO>> getAccountByUsername(@PathVariable String username) {
        log.info("Fetching list of accounts for: {}", username);
        return new ResponseEntity<>(accountService.getAllAccountByUsername(username), HttpStatus.OK);
    }


    @GetMapping("/count")
    public ResponseEntity<Long> getCountOfAllAccounts() {
        log.info("Fetching count of accounts");
        return new ResponseEntity<>(accountService.getCountAllAccount(), HttpStatus.OK);
    }


    @GetMapping("/activeCount")
    public ResponseEntity<Long> getCountOfAllActiveAccounts() {
        log.info("Fetching count of all active accounts");
        return new ResponseEntity<>(accountService.getCountAllActiveAccount(), HttpStatus.OK);
    }


    @GetMapping("/inactiveCount")
    public ResponseEntity<Long> getCountOfAllInactiveAccounts() {
        log.info("Fetching count of all inactive accounts");
        return new ResponseEntity<>(accountService.getCountAllInactiveAccount(), HttpStatus.OK);
    }

    @PostMapping("/fundTransfer")
    public ResponseEntity<String> fundTransfer(@Valid @RequestBody FundTransferRequest request) {
        log.info("Fund Transfer process is initiated");
        return new ResponseEntity<>(accountService.fundTransfer(request), HttpStatus.OK);
    }

    @PostMapping("/openAccount")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> openAccount(@Valid @RequestBody NewAccountRequest request) {
        log.info("Opening account process is initiated");
        return new ResponseEntity<>(accountService.openAccount(request), HttpStatus.CREATED);
    }

    @GetMapping("/checkBalance")
    public ResponseEntity<Double> checkBalance(@RequestParam Long accountNo) {
        log.info("Checking balance for account: {}", accountNo);
        return new ResponseEntity<>(accountService.checkingBalance(accountNo), HttpStatus.OK);
    }

    @GetMapping("/miniStatement")
    public ResponseEntity<List<TransactionDTO>> miniStatement(@RequestParam Long accountNo) {
        log.info("Generating mini statement for account: {}", accountNo);
        return new ResponseEntity<>(accountService.miniStatement(accountNo), HttpStatus.OK);
    }

    @GetMapping("/detailedStatement")
    public ResponseEntity<List<TransactionDTO>> detailedStatement(@RequestParam Long accountNo,
                                                                  @RequestParam(name = "startDate") String startDate,
                                                                  @RequestParam(name = "endDate") String endDate) {
        log.info("Generating detailed statement for account: {}", accountNo);
        return new ResponseEntity<>(accountService.detailedStatement(accountNo, startDate, endDate), HttpStatus.OK);
    }

    @PostMapping("/pinChange")
    public ResponseEntity<String> pinChange(@Valid @RequestBody CardPinChangeRequest request) {
        log.info("Card pin change request for: {}", request.getCardNo());
        return new ResponseEntity<>(accountService.pinChange(request), HttpStatus.OK);
    }

    @PostMapping("/addDebitCard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> addNewDebitCard(@Valid @RequestBody NewDebitCardRequest request) {
        return new ResponseEntity<>(accountService.addNewDebitCard(request), HttpStatus.OK);
    }

    @PostMapping("/cardBlock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> blockDebitOrCreditCard(@Valid @RequestBody BlockDebitOrCreditCardRequest request) {
        return new ResponseEntity<>(accountService.blockDebitOrCreditCard(request), HttpStatus.OK);
    }

}
