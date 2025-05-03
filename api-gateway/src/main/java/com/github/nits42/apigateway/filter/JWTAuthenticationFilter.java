package com.github.nits42.apigateway.filter;

import com.github.nits42.apigateway.exceptions.BankingAppApiGatewayException;
import com.github.nits42.apigateway.security.jwt.JWTUtil;
import com.github.nits42.apigateway.util.AppConstant;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;


/**
 * JWTAuthenticationFilter is a Spring Cloud Gateway filter that handles JWT authentication for incoming requests.
 * It checks if the request contains a valid JWT token in the Authorization header.
 * If the token is missing or invalid, it throws a BankingAppApiGatewayException with an appropriate error message.
 * The filter is applied to secured routes defined in the RouteValidator class.
 * If the token is valid, the filter allows the request to proceed to the next filter or endpoint.
 * If the token is invalid or missing, the filter throws an exception and returns an error response to the client.
 * The filter also logs information about the JWT token validation process.
 */


@Slf4j
@Component
public class JWTAuthenticationFilter extends AbstractGatewayFilterFactory<JWTAuthenticationFilter.Config> {

    private final JWTUtil jwtUtil;

    private final RouteValidator routeValidator;

    /**
     * Constructor for JWTAuthenticationFilter.
     *
     * @param jwtUtil       The JWT utility class for token validation.
     * @param routeValidator The RouteValidator instance to check if the route is secured.
     */

    @Autowired
    public JWTAuthenticationFilter(JWTUtil jwtUtil, RouteValidator routeValidator) {
        super(Config.class);
        this.jwtUtil = jwtUtil;
        this.routeValidator = routeValidator;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            log.info("Requested URI: {}", exchange.getRequest().getURI());
            ServerHttpRequest request = exchange.getRequest();

            if (routeValidator.isApiSecured.test(request)) {
                if (isAuthMissing(request)) {
                    log.error(AppConstant.AUTHORIZATION_HEADER_MISSING);
                    throw BankingAppApiGatewayException.builder()
                            .message(AppConstant.AUTHORIZATION_HEADER_MISSING)
                            .status(HttpStatus.BAD_REQUEST)
                            .build();
                }
                Optional<String> jwtToken = extractJwtFromRequest(request);
                if (jwtToken.isPresent()) {
                    try {
                        jwtUtil.validateToken(jwtToken.get(), request);

                    } catch (IllegalArgumentException | MalformedJwtException | ExpiredJwtException ex) {
                        //return onError(exchange);
                        if (ex instanceof ExpiredJwtException) {
                            log.error(AppConstant.TOKEN_EXPIRED);
                            throw BankingAppApiGatewayException.builder()
                                    .message(AppConstant.TOKEN_EXPIRED)
                                    .status(HttpStatus.UNAUTHORIZED)
                                    .build();
                        } else if (ex instanceof IllegalArgumentException) {
                            log.error(AppConstant.INVALID_TOKEN);
                            throw BankingAppApiGatewayException.builder()
                                    .message(AppConstant.INVALID_TOKEN)
                                    .status(HttpStatus.UNAUTHORIZED)
                                    .build();
                        } else {
                            log.error(AppConstant.INVALID_TOKEN);
                            throw BankingAppApiGatewayException.builder()
                                    .message(AppConstant.INVALID_TOKEN)
                                    .status(HttpStatus.UNAUTHORIZED)
                                    .build();
                        }
                    }
                }
            }
            log.info("JWT Authentication Filter applied.");
            return chain.filter(exchange);
        });
    }


    private Mono<Void> onError(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    private boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey(AppConstant.AUTHORIZATION);
    }

    private Optional<String> extractJwtFromRequest(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getOrEmpty(AppConstant.AUTHORIZATION).getFirst();
        if (bearerToken != null && bearerToken.startsWith(AppConstant.BEARER)) {
            return Optional.of(bearerToken.substring(AppConstant.BEARER.length()));
        } else {
            log.error(AppConstant.JWT_TOKEN_MISSING);
            throw BankingAppApiGatewayException.builder()
                    .message(AppConstant.JWT_TOKEN_MISSING)
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }
    public static class Config {
        // Add configuration properties if needed
    }
}
