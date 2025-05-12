package com.github.nits42.atmservice.entities;

import com.github.nits42.atmservice.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction extends BaseEntity {

    @Column(nullable = false, updatable = false)
    private double amount;

    @Column(updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false, updatable = false)
    private Account account; // Each Transaction belongs to one account

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime transactionDate;

}
