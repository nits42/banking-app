package com.github.nits42.userservice.config;

import com.github.nits42.userservice.security.RoleCheckFilter;
import com.github.nits42.userservice.util.AppConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true) -- Deprecated from Spring Security 5.7
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final RoleCheckFilter roleCheckFilter;

    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF protection
                .cors(AbstractHttpConfigurer::disable) // Disable CORS
                .httpBasic(AbstractHttpConfigurer::disable) // Disable basic authentication
                .formLogin(AbstractHttpConfigurer::disable) // Disable form login
                .authorizeHttpRequests(authorize ->
                        authorize
                                .anyRequest().permitAll()
                ).sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ).addFilterBefore(roleCheckFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout ->
                        logout
                                .logoutUrl(AppConstant.LOGOUT_URL)
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler(
                                        (request,
                                         response,
                                         authentication
                                        ) -> SecurityContextHolder.clearContext()
                                )
                );

        return http.build();
    }
}
