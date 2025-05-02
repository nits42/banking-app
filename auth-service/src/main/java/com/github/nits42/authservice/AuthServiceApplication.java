package com.github.nits42.authservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
public class AuthServiceApplication {

    // This is the main class for the Spring Boot Auth-Service application.
    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }

}
