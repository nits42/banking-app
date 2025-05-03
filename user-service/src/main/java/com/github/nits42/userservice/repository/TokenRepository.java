package com.github.nits42.userservice.repository;

import com.github.nits42.userservice.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, UUID> {

    @Query("""
            SELECT t FROM Token t inner join User u on t.user.id = u.id
            WHERE u.id = :userId AND (t.expired = false or t.revoked = false)
            """)
    Set<Token> findAllValidTokenByUser(UUID userId);

    Optional<Token> findByToken(String token);

    Optional<Token> findByTokenAndExpired(String token, boolean isExpired);

    Set<Token> findByExpiredAndRevoked(boolean isExpired, boolean isRevoked);

}
