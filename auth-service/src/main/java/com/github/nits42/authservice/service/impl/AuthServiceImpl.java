package com.github.nits42.authservice.service.impl;

import com.github.nits42.authservice.clients.UserServiceFeignClient;
import com.github.nits42.authservice.exceptions.BankingAppAuthServiceApiException;
import com.github.nits42.authservice.request.AuthRequest;
import com.github.nits42.authservice.request.LoginRequest;
import com.github.nits42.authservice.service.AuthService;
import com.github.nits42.authservice.util.AppConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final RestTemplate restTemplate;
    private final UserServiceFeignClient userServiceFeignClient;
    private final AuthenticationManager authenticationManager;


    @Override
    public String registerUser(AuthRequest request, String requestFrom) {
        // Implement the logic to register a user
        // For example, save the user details to a database
/*

        String userServiceUrl = "http://user-service/v1/users";
        String userServiceUrl = "http://localhost:8084/v1/users";
        Use RestTemplate to send a POST request to the user service
        PostForObject() - return only response body
        PostForEntity() - return response entity (response body + headers + status code)
        return restTemplate.postForEntity(userServiceUrl, request, String.class).getBody();
*/

        request.setRequestFrom(requestFrom);
        return userServiceFeignClient.createUser(request).getBody();
    }

    @Override
    public String login(LoginRequest request) {
        // Implement the logic to authenticate a user
        Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        if (!authenticate.isAuthenticated()) {
            throw BankingAppAuthServiceApiException.builder().message(AppConstant.INVALID_CREDENTIALS).build();
        }
        return "Login successful";
    }
}
