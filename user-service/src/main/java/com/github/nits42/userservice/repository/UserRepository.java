package com.github.nits42.userservice.repository;

import com.github.nits42.userservice.entities.User;
import com.github.nits42.userservice.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

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
