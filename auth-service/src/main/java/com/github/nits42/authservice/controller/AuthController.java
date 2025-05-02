package com.github.nits42.authservice.controller;

import com.github.nits42.authservice.dto.TokenDTO;
import com.github.nits42.authservice.request.LoginRequest;
import com.github.nits42.authservice.request.UserRegisterRequest;
import com.github.nits42.authservice.service.AuthService;
import com.github.nits42.authservice.util.AppConstant;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(AppConstant.AUTH_SERVICE_BASE_URL)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping({"/register", "/signup"})
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterRequest request, HttpServletRequest httpServletRequest) {
        log.info("Register request originated from \"{}\" for user: {}", httpServletRequest.getRequestURI(), request.getUsername());
        String response = authService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping({"/login", "/signin"})
    public ResponseEntity<TokenDTO> login(@RequestBody LoginRequest request, HttpServletRequest httpServletRequest) {
        log.info("Login request: {}", request.getUsername());
        return new ResponseEntity<>(authService.login(request, httpServletRequest), HttpStatus.OK);
    }

}
