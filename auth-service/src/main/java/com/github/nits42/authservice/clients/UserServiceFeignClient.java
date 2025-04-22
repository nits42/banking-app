package com.github.nits42.authservice.clients;

import com.github.nits42.authservice.dto.UserDTO;
import com.github.nits42.authservice.request.AuthRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service", url = "http://localhost:8084/v1/users")
public interface UserServiceFeignClient {

    @PostMapping
    ResponseEntity<String> createUser(@RequestBody AuthRequest request);

    @GetMapping("/{username}")
    ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username);

    @GetMapping("/{email}")
    ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email);


}
