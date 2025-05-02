package com.github.nits42.authservice.config;

import com.github.nits42.authservice.exceptions.BankingAppAuthServiceApiException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class FeignConfig {
    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }

    static class CustomErrorDecoder implements ErrorDecoder {

        private final ErrorDecoder defaultErrorDecoder = new Default();

        @Override
        public Exception decode(String methodKey, Response response) {
            if (response.body() != null) {
                try {
                    // Extract the error body and handle it specifically
                    String body = new String(response.body().asInputStream().readAllBytes());
                    return BankingAppAuthServiceApiException.builder()
                            .message("Feign Error: " + body)
                            .status(HttpStatus.valueOf(response.status()))
                            .build();
                } catch (Exception e) {
                    return new Exception("Feign Error with empty body: " + response.status(), e);
                }
            }
            return defaultErrorDecoder.decode(methodKey, response);
        }

    }
}