package com.github.nits42.atmservice.repository;


import com.github.nits42.atmservice.entities.Card;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CardRepository extends JpaRepository<Card, UUID> {

    Optional<Card> findByCardNumberAndSecurityPin(@NotBlank(message = "ATM card number is required") long cardNo,
                                                  @NotBlank(message = "Current pin is required ATM card number") int pin);
}
