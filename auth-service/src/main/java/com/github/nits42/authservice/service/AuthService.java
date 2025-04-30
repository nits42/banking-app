package com.github.nits42.authservice.service;

import com.github.nits42.authservice.request.AuthRequest;
import com.github.nits42.authservice.request.LoginRequest;

public interface AuthService {
    String registerUser(AuthRequest request, String requestFrom);

    String login(LoginRequest request);
}
