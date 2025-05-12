package com.github.nits42.atmservice.client;

import com.github.nits42.atmservice.dto.TransactionDTO;
import com.github.nits42.atmservice.dto.UserDto;
import com.github.nits42.atmservice.request.AccountUpdateRequest;
import com.github.nits42.atmservice.request.CardPinChangeRequest;
import com.github.nits42.atmservice.request.FundTransferRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "core-banking-service", path = "/v1/account")
public interface CoreBankingServiceClient {

    @GetMapping("/username/{username}")
    ResponseEntity<UserDto> getAccountByUsername(@PathVariable String username);

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestBody AccountUpdateRequest request);

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody AccountUpdateRequest request);

    @GetMapping("/checkBalance")
    public ResponseEntity<Double> checkBalance(@RequestParam Long accountNo);

    @GetMapping("/miniStatement")
    public ResponseEntity<List<TransactionDTO>> miniStatement(@RequestParam Long accountNo);

    @PostMapping("/fundTransfer")
    public ResponseEntity<String> fundTransfer(@RequestBody FundTransferRequest request);

    @PostMapping("/pinChange")
    public ResponseEntity<String> pinChange(@RequestBody CardPinChangeRequest request);

}
