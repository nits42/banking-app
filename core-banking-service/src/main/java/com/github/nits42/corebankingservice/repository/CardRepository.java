package com.github.nits42.corebankingservice.repository;

import com.github.nits42.corebankingservice.entities.Card;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, String> {

    Optional<Card> findByCardNumberAndSecurityPin(@NotBlank(message = "ATM card number is required") long cardNo,
                                                  @NotBlank(message = "Current pin is required ATM card number") int pin);

}
