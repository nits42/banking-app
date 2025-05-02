package com.github.nits42.apigateway.security.jwt;

import com.github.nits42.apigateway.exceptions.InvalidTokenException;
import com.github.nits42.apigateway.util.AppConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

/**
 * JWTUtil is a utility class for generating and validating JSON Web Tokens (JWTs).
 * It provides methods to create tokens, extract claims, and validate tokens.
 */
@Component
@RequiredArgsConstructor
public class JWTUtil {

//    TODO: 1. Add IP address to JWT
//    TODO: 2. Add USER_AGENT to JWT
//    TODO: 3. Add Logout functionality

    @Value("${security.jwt.secret-key}")
    public String SECRET_KEY;

    private static String getClientIp(ServerHttpRequest request) {
        String remoteAddr = "";
        if (request != null) {
            remoteAddr = request.getRemoteAddress().getAddress().getHostAddress();
        }
        return remoteAddr;
    }

    public static String getUserAgent(ServerHttpRequest request) {
        String ua = "";
        if (request != null) {
            ua = request.getHeaders().getFirst("user-agent");
        }
        return ua;
    }

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

    public void validateToken(final String token, ServerHttpRequest request) {
        final String ipAddress = getClientIp(request);
        final String userAgent = getUserAgent(request);

        if (!(ipAddress.equals(getClientIp(token)) && userAgent.equals(getUserAgent(token)))) {
            throw new InvalidTokenException(AppConstant.INVALID_TOKEN);
        }

        final String username = getUsernameFromToken(token);
        if (username == null || username.isEmpty()) {
            throw new InvalidTokenException(AppConstant.INVALID_TOKEN);
        }
        isTokenExpired(token);
    }

    private void isTokenExpired(final String token) {
        if (getExpirationFromToken(token).before(new Date())) {
            throw new ExpiredJwtException(null, null, AppConstant.TOKEN_EXPIRED);
        }
    }

    private Date getExpirationFromToken(String token) {
        return getClaimsFromToken(token, Claims::getExpiration);
    }

    private String getUsernameFromToken(final String token) {
        return getClaimsFromToken(token, Claims::getSubject);
    }

    private String getUserIdFromToken(final String token) {
        return getClaimsFromToken(token, claims -> claims.get(AppConstant.USER_ID, String.class));
    }

    private String getEmailFromToken(final String token) {
        return getClaimsFromToken(token, claims -> claims.get(AppConstant.EMAIL, String.class));
    }

    private String getRoleFromToken(final String token) {
        return getClaimsFromToken(token, claims -> claims.get(AppConstant.ROLES, String.class));
    }

    public String getClientIp(String token) {
        return getClaimsFromToken(token, claims -> claims.get(AppConstant.IP)).toString();
    }

    public String getUserAgent(String token) {
        return getClaimsFromToken(token, claims -> claims.get(AppConstant.USER_AGENT)).toString();
    }


}
