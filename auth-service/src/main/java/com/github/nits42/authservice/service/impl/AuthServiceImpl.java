package com.github.nits42.authservice.service.impl;

import com.github.nits42.authservice.clients.UserServiceFeignClient;
import com.github.nits42.authservice.dto.TokenDTO;
import com.github.nits42.authservice.enums.Role;
import com.github.nits42.authservice.exceptions.BankingAppAuthServiceApiException;
import com.github.nits42.authservice.exceptions.WrongCredentialsException;
import com.github.nits42.authservice.request.LoginRequest;
import com.github.nits42.authservice.request.SigupRequest;
import com.github.nits42.authservice.request.TokenRequest;
import com.github.nits42.authservice.request.UserRegisterRequest;
import com.github.nits42.authservice.security.jwt.JWTUtil;
import com.github.nits42.authservice.service.AuthService;
import com.github.nits42.authservice.util.AppConstant;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final RestTemplate restTemplate;
    private final UserServiceFeignClient userServiceFeignClient;
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    @Override
    public String register(UserRegisterRequest request) {
        // Implement the logic to register a user,
        // For example, save the user details to a database
/*

        String userServiceUrl = "http://user-service/v1/users";
        String userServiceUrl = "http://localhost:8084/v1/users";
        Use RestTemplate to send a POST request to the user service
        PostForObject() - return only response body
        PostForEntity() - return response entity (response body + headers + status code)
        return restTemplate.postForEntity(userServiceUrl, request, String.class).getBody();
*/
        SigupRequest sigupRequest = SigupRequest.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .email(request.getEmail())
                .role(Role.ADMIN)
                .build();

        // Call the user service to create a new user
        ResponseEntity<String> response = userServiceFeignClient.createUser(sigupRequest);
        if (response.getBody() == null) {
            throw BankingAppAuthServiceApiException.builder()
                    .message(AppConstant.USER_REGISTRATION_FAILED)
                    .build();
        }
        return response.getBody();
    }

    @Override
    public TokenDTO login(LoginRequest request, HttpServletRequest httpServletRequest) {
        // Implement the logic to authenticate a user
        Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        if (!authenticate.isAuthenticated()) {
            throw BankingAppAuthServiceApiException.builder()
                    .message(AppConstant.INVALID_CREDENTIALS)
                    .build();
        }
        // Generate JWT token and return it
        var token = jwtUtil.generateToken(request.getUsername(), httpServletRequest);

        // Save the token to the user service
        ResponseEntity<String> response = userServiceFeignClient.saveToken(TokenRequest.builder()
                .username(request.getUsername())
                .token(token)
                .build()
        );
        if (response.getStatusCode().equals(HttpStatus.CREATED)) {
            log.debug("Generated token: {}", token);
            return TokenDTO
                    .builder()
                    .token(token)
                    .build();
        } else
            throw new WrongCredentialsException(AppConstant.INVALID_CREDENTIALS);

    }
}
