package com.github.nits42.corebankingservice.clients;

import com.github.nits42.corebankingservice.dto.UserDTO;
import com.github.nits42.corebankingservice.request.UserSignupRequest;
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
    ResponseEntity<UserDTO> createCustomer(@Valid @RequestBody UserSignupRequest request);

    @GetMapping("/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username);

    @GetMapping("/id/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable UUID id);
}
