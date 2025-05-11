package com.github.nits42.corebankingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
@EnableDiscoveryClient
public class CoreBankingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoreBankingServiceApplication.class, args);
    }

}
