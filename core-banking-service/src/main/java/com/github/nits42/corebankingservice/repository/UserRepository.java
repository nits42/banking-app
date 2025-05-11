package com.github.nits42.corebankingservice.repository;

import com.github.nits42.corebankingservice.entities.User;
import com.github.nits42.corebankingservice.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<User> findByStatus(Status status);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameAndStatus(String username, Status status);

    Optional<User> findByEmailAndStatus(String email, Status status);

    Optional<User> findByIdAndStatus(UUID id, Status status);

}
