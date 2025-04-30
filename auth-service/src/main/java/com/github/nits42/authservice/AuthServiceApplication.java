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
        System.out.println("Password for password..." +
                new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode("password"));
        System.out.println("Password for admin123..." +
                new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode("admin123"));
    }

}
