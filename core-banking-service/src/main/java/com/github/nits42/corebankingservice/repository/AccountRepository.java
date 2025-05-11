package com.github.nits42.corebankingservice.repository;

import com.github.nits42.corebankingservice.entities.Account;
import com.github.nits42.corebankingservice.enums.Status;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {

    /*@Modifying
    @Query("update Account set balance = balance + :amount where id = :id")
    void deposit(@Param("id") String id, @Param("amount") Double amount);

    @Modifying
    @Query("update Account set balance = balance - :amount where id = :id")
    void withdraw(@Param("id") String id, @Param("amount") Double amount);*/

    Optional<Account> findByAccountNumber(@NotBlank(message = "accountNo is required") Long accountNo);

    List<Account> findAllByStatus(Status status);
}
