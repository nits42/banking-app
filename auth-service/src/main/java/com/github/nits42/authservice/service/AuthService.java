package com.github.nits42.authservice.service;

import com.github.nits42.authservice.request.AuthRequest;

public interface AuthService {
    String registerUser(AuthRequest request);
}
