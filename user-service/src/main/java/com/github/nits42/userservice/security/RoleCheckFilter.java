package com.github.nits42.userservice.security;

import com.github.nits42.userservice.exceptions.BankingAppUserServiceException;
import com.github.nits42.userservice.security.jwt.JWTUtil;
import com.github.nits42.userservice.util.AppConstant;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoleCheckFilter extends OncePerRequestFilter {

//    TODO: Add check for token for logout

    private final JWTUtil jwtUtil;

    private static Optional<String> getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AppConstant.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(AppConstant.JWT_BEARER)) {
            return Optional.of(bearerToken.substring(7));
        }
        return Optional.empty();
    }

    private void setSecurityContext(WebAuthenticationDetails authDetails, String token) {
        final String username = jwtUtil.getUsernameFromToken(token);
        final String roles = jwtUtil.getRoleFromToken(token);
        System.out.println("User's Role: " + roles);
        final UserDetails userDetails = new User(
                username,
                "",
                Collections.singleton(new SimpleGrantedAuthority(roles))
        );

        final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),
                null,
                userDetails.getAuthorities()
        );
        authentication.setDetails(authDetails);
        // After setting the Authentication in the context, we specify
        // that the current user is authenticated. So it passes the
        // Spring Security Configurations successfully.
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        final Optional<String> jwt = getJwtFromRequest(request);

        jwt.ifPresent(token -> {
            try {
                // Validate the token
                setSecurityContext(new WebAuthenticationDetailsSource().buildDetails(request), token);
            } catch (IllegalArgumentException | MalformedJwtException | ExpiredJwtException ex) {
                //return onError(exchange);
                if (ex instanceof ExpiredJwtException) {
                    log.error(AppConstant.TOKEN_EXPIRED);
                    throw BankingAppUserServiceException.builder()
                            .message(AppConstant.TOKEN_EXPIRED)
                            .httpStatus(HttpStatus.UNAUTHORIZED)
                            .build();
                } else if (ex instanceof IllegalArgumentException) {
                    log.error(AppConstant.INVALID_TOKEN);
                    throw BankingAppUserServiceException.builder()
                            .message(AppConstant.INVALID_TOKEN)
                            .httpStatus(HttpStatus.UNAUTHORIZED)
                            .build();
                } else {
                    log.error(AppConstant.INVALID_TOKEN);
                    throw BankingAppUserServiceException.builder()
                            .message(AppConstant.INVALID_TOKEN)
                            .httpStatus(HttpStatus.UNAUTHORIZED)
                            .build();
                }
            }
        });
        filterChain.doFilter(request, response);
    }

}