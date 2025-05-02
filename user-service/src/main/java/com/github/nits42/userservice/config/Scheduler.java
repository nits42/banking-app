package com.github.nits42.userservice.config;

import com.github.nits42.userservice.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class Scheduler {

    private final TokenRepository tokenRepository;

    @Scheduled(fixedDelay = 3600000, initialDelay = 60000)
    private void updateTokenExpiredFlag() {
        long start = System.currentTimeMillis();
        log.info("Schedule job to check token's expiration time is started");
        var tokens = tokenRepository.findByExpiredAndRevoked(false, false);
        if (tokens.isEmpty()) {
            log.info("Schedule job to check token's expiration time is completed in {} seconds", (System.currentTimeMillis() - start) / 1000);
            return;
        }
        tokens.forEach(token -> {
            if (token.getCreatedAt().plusHours(1).isBefore(LocalDateTime.now()) && !token.isExpired())
                token.setExpired(true);
        });
        tokenRepository.saveAll(tokens);
        log.info("Schedule job to check token's expiration time is completed in {} seconds", (System.currentTimeMillis() - start) / 1000);
    }

}
