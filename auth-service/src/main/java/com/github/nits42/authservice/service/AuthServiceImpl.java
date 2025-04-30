package com.github.nits42.authservice.service;

import com.github.nits42.authservice.clients.UserServiceFeignClient;
import com.github.nits42.authservice.request.AuthRequest;
import com.github.nits42.authservice.request.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthServiceImpl implements AuthService {

    private final RestTemplate restTemplate;
    private final UserServiceFeignClient userServiceFeignClient;

    @Autowired
    public AuthServiceImpl(RestTemplate restTemplate,
                           UserServiceFeignClient userServiceFeignClient) {
        this.restTemplate = restTemplate;
        this.userServiceFeignClient = userServiceFeignClient;
    }


    @Override
    public String registerUser(AuthRequest request, String requestFrom) {
        // Implement the logic to register a user
        // For example, save the user details to a database

//        String userServiceUrl = "http://user-service/v1/users";
        String userServiceUrl = "http://localhost:8084/v1/users";
        // Use RestTemplate to send a POST request to the user service
        //PostForObject() - return only response body
        //PostForEntity() - return response entity (response body + headers + status code)
        //return restTemplate.postForEntity(userServiceUrl, request, String.class).getBody();
        request.setRequestFrom(requestFrom);
        return userServiceFeignClient.createUser(request).getBody();
    }

    @Override
    public String login(LoginRequest request) {
        // Implement the logic to authenticate a user
        
        userServiceFeignClient.getUserByUsername(request.getUsername());
        return "";
    }
}
