package com.github.nits42.corebankingservice.clients;

import com.github.nits42.corebankingservice.dto.UserDTO;
import com.github.nits42.corebankingservice.request.AccountOpeningRequest;
import com.github.nits42.corebankingservice.util.AppConstant;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

/**
 * Author: Nitin Kumar
 * Date:11/05/25
 * Time:15:31
 */

@FeignClient(name = "user-service", path = AppConstant.USER_SERVICE_BASE_URL)
public interface UserServiceFeignClient {

    @PostMapping("/createCustomer")
    ResponseEntity<UserDTO> createCustomer(@Valid @RequestBody AccountOpeningRequest request);

    @GetMapping("/{username}")
    ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username);

    @GetMapping("/email/{email}")
    ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email);

    @GetMapping("/id/{id}")
    ResponseEntity<UserDTO> getUserById(@PathVariable UUID id);
}
