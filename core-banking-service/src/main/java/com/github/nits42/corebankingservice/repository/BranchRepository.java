package com.github.nits42.corebankingservice.repository;

import com.github.nits42.corebankingservice.entities.Branch;
import com.github.nits42.corebankingservice.enums.Status;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BranchRepository extends JpaRepository<Branch, UUID> {

    Optional<Branch> findByBranchName(@NotBlank(message = "Branch name is required") String branchName);

    List<Branch> findAllByStatus(Status status);

    Optional<Branch> findByIdAndStatus(UUID branchId, Status status);

    List<Branch> findAllByStatusAndPostalCode(Status status, int postalCode);

    boolean existsByBranchName(@NotBlank(message = "Branch name is required") String branchName);
}
