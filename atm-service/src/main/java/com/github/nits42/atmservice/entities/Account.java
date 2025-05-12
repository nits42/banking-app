package com.github.nits42.atmservice.entities;

import com.github.nits42.atmservice.enums.AccountType;
import com.github.nits42.atmservice.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Account extends BaseEntity {

    @Column(updatable = false, unique = true, nullable = false)
    private Long accountNumber;

    private double balance;

    @Column(updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user; // Each account belongs to one user

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Card> cards; // Each Account can have multiple cards

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Transaction> transactions; // Each Account can have multiple transactions

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

    public void deposit(double amount) {
        this.balance += amount;
    }

    public void withdraw(double amount) {
        if (this.balance >= amount) {
            this.balance -= amount;
        } else {
            throw new IllegalArgumentException("Insufficient balance");
        }
    }

}
