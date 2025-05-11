package com.github.nits42.corebankingservice.repository;

import com.github.nits42.corebankingservice.entities.Account;
import com.github.nits42.corebankingservice.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {

    //    @Query("SELECT t FROM Transaction t WHERE t.account = :account ORDER BY t.transactionDate DESC LIMIT 10")
    List<Transaction> findFirst10ByAccountOrderByTransactionDateDesc(Account account);

    List<Transaction> findByAccountAndTransactionDateBetweenOrderByTransactionDateDesc(Account account, LocalDateTime start, LocalDateTime end);
}
