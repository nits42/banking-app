package com.github.nits42.authservice.service;

import com.github.nits42.authservice.dto.TokenDTO;
import com.github.nits42.authservice.request.LoginRequest;
import com.github.nits42.authservice.request.UserRegisterRequest;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {

    String register(UserRegisterRequest request);

    TokenDTO login(LoginRequest request, HttpServletRequest httpServletRequest);

}
