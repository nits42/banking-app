package com.github.nits42.authservice.clients;

import com.github.nits42.authservice.dto.UserDTO;
import com.github.nits42.authservice.request.SigupRequest;
import com.github.nits42.authservice.request.TokenRequest;
import com.github.nits42.authservice.util.AppConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service", path = AppConstant.USER_SERVICE_BASE_URL)
public interface UserServiceFeignClient {

    @PostMapping
    ResponseEntity<String> createUser(@RequestBody SigupRequest request);

    @GetMapping("/{username}")
    ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username);

    @GetMapping("/{email}")
    ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email);

    @PostMapping("/token")
    ResponseEntity<String> saveToken(@RequestBody TokenRequest request);


}
