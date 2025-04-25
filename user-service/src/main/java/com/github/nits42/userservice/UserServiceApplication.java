package com.github.nits42.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class UserServiceApplication {

    // This is the main class for the Spring Boot User-Service application.
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

}
