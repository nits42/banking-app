package com.github.nits42.atmservice.entities;

import com.github.nits42.atmservice.enums.CardProvider;
import com.github.nits42.atmservice.enums.CardType;
import com.github.nits42.atmservice.enums.Status;
import com.github.nits42.atmservice.utils.CardUtils;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Card extends BaseEntity {

    @Column(updatable = false, unique = true)
    private Long cardNumber;

    @Column(updatable = false, nullable = false)
    private String cardHolderName;

    @Column(updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    private CardProvider provider;

    @Column(updatable = false, nullable = false)
    private LocalDate cardExpiry;

    @Column(updatable = false, nullable = false)
    private int cvv;

    @Column(nullable = false)
    private int securityPin;

    @Column(updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    private CardType cardType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "account_id", updatable = false, nullable = false)
    private Account account; // Each account belongs to one card

    @PrePersist
    private void prePersist() {
        // Generate a random CVV
        this.cvv = CardUtils.generateCVV();

        // Generate a random PIN
        this.securityPin = CardUtils.generatePIN();

        // Set expiry date to 5 years from the current date
        this.cardExpiry = LocalDate.now().plusYears(5);

        // Choose a random provider
        this.provider = CardProvider.valueOf(CardUtils.generateProvider().toUpperCase());

        // Generate a random card number
        this.cardNumber = CardUtils.generateCardNumber();

    }

}
