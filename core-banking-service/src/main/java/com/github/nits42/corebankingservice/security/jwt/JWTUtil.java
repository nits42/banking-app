package com.github.nits42.corebankingservice.security.jwt;

import com.github.nits42.corebankingservice.repository.TokenRepository;
import com.github.nits42.corebankingservice.util.AppConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.function.Function;

/**
 * JWTUtil is a utility class for generating and validating JSON Web Tokens (JWTs).
 * It provides methods to create tokens, extract claims, and validate tokens.
 */
@Component
@RequiredArgsConstructor
public class JWTUtil {

    private final TokenRepository jwtTokenRepository;
    @Value("${security.jwt.secret-key}")
    public String SECRET_KEY;

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private <T> T getClaimsFromToken(final String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // extract claims from token
    private Claims extractAllClaims(final String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUsernameFromToken(final String token) {
        return getClaimsFromToken(token, Claims::getSubject);
    }

    public String getRoleFromToken(final String token) {
        return getClaimsFromToken(token, claims -> claims.get(AppConstant.ROLES, String.class));
    }

    //Checking is a token expired or revoked
    public boolean isTokenValid(String token) {
        return jwtTokenRepository.findByToken(token)
                .map(t -> !t.isExpired() && !t.isRevoked())
                .orElse(false);
    }
}
