package com.github.nits42.authservice.security.jwt;

import com.github.nits42.authservice.security.CustomUserDetails;
import com.github.nits42.authservice.security.CustomUserDetailsService;
import com.github.nits42.authservice.util.AppConstant;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWTUtil is a utility class for generating and validating JSON Web Tokens (JWTs).
 * It provides methods to create tokens, extract claims, and validate tokens.
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTUtil {

    private final CustomUserDetailsService customUserDetailService;
    @Value("${security.jwt.secret-key}")
    private String SECRET_KEY;
    @Value("${security.jwt.expiration-time}")
    private long EXPIRATION_TIME;

    private static String getClientIp(HttpServletRequest request) {
        String clientIp = "";
        if (request != null) {
            clientIp = request.getHeader("X-FORWARDED-FOR");
            if (clientIp == null || clientIp.isEmpty()) {
                clientIp = request.getRemoteAddr();
            }
        }
        log.debug("Client IP: {}", clientIp);
        return clientIp;
    }

    private static String getUserAgent(HttpServletRequest request) {
        String userAgent = "";
        if (request != null) {
            userAgent = request.getHeader("User-Agent");
        }
        log.debug("User Agent: {}", userAgent);
        return userAgent;
    }

    public String generateToken(String username, HttpServletRequest httpServletRequest) {
        // Generate a JWT token using the secret key and expiration time
        CustomUserDetails customUserDetails = (CustomUserDetails) customUserDetailService.loadUserByUsername(username);

        // Create a map to hold the claims
        Map<String, Object> claims = new HashMap<>();

        // add a user's role to claims
        claims.put(AppConstant.ROLES, customUserDetails.getAuthorities().stream().iterator().next().getAuthority());

        // add user ID to claims
        claims.put(AppConstant.USER_ID, customUserDetails.getUserId());

        // add user email to claims
        claims.put(AppConstant.EMAIL, customUserDetails.getEmail());

        // add client ip address to claims
        claims.put(AppConstant.IP, getClientIp(httpServletRequest));

        // add user agent to claims
        claims.put(AppConstant.USER_AGENT, getUserAgent(httpServletRequest));

        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String username) throws InvalidKeyException {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expirationTime = new Date(nowMillis + EXPIRATION_TIME);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuer(AppConstant.ISSUER)
                .setIssuedAt(now)
                .setExpiration(expirationTime)
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    protected Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
