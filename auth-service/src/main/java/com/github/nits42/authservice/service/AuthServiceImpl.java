package com.github.nits42.authservice.service;

import com.github.nits42.authservice.clients.UserServiceFeignClient;
import com.github.nits42.authservice.request.AuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
//@RequiredArgsConstructor
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
    public String registerUser(AuthRequest request) {
        // Implement the logic to register a user
        // For example, save the user details to a database

//        String userServiceUrl = "http://user-service/v1/users";
        String userServiceUrl = "http://localhost:8084/v1/users";
        // Use RestTemplate to send a POST request to the user service
        //PostForObject() - return only response body
        //PostForEntity() - return response entity (response body + headers + status code)
        //return restTemplate.postForEntity(userServiceUrl, request, String.class).getBody();
        return userServiceFeignClient.createUser(request).getBody();
    }
}
