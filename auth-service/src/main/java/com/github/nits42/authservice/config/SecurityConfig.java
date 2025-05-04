package com.github.nits42.authservice.config;

import com.github.nits42.authservice.security.CustomUserDetailsService;
import com.github.nits42.authservice.util.AppConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ConditionalOnProperty(value = "security.enabled", havingValue = "true")
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailService;

    // Security configuration goes here
    // This class can be used to configure security settings for the application,
    // For example, you can configure authentication providers, password encoders, etc.
    // You can also define security filters and other security-related beans here

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(AppConstant.AUTH_SERVICE_BASE_URL + "/**").permitAll()
                        .requestMatchers(
                                AppConstant.API_DOCS_URLS,
                                AppConstant.SWAGGER_UI_URLS,
                                AppConstant.SWAGGER_UI_HTML_URLS,
                                AppConstant.AUTH_SERVICE_API_DOC_URL,
                                AppConstant.ACTUATOR_HEALTH_URL,
                                AppConstant.EUREKA_URL
                        ).permitAll()
                        .anyRequest().authenticated()
                ).sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        return http.build();
    }

    // For Password Encoding
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // For Authentication Manager
    @Bean
    public AuthenticationManager authenticationManager(final AuthenticationConfiguration authConfiguration) throws Exception {
        return authConfiguration.getAuthenticationManager();
    }


}
