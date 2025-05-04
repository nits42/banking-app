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

    // To trigger the scheduler every one minute
    // between 19:00 PM to 19:59 PM
    //@Scheduled(cron = "0 * 19 * * ?")

    // To trigger the scheduler to run every two seconds
    //@Scheduled(fixedRate = 2000)

    // To trigger the scheduler every 1 hour with
    // an initial delay of 1 Minute.
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
        if ((System.currentTimeMillis() - start) / 1000 <= 0)
            log.info("Schedule job to check token's expiration time is completed in {} milliseconds", (System.currentTimeMillis() - start));
        else
            log.info("Schedule job to check token's expiration time is completed in {} seconds", (System.currentTimeMillis() - start) / 1000);
    }

}
