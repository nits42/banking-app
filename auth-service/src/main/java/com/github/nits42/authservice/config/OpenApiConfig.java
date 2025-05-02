package com.github.nits42.authservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@OpenAPIDefinition
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI(
            @Value("${openapi.service.title}") String title,
            @Value("${openapi.service.version}") String version,
            @Value("${openapi.service.description}") String description,
            @Value("${openapi.service.contact}") String contact,
            @Value("${openapi.service.license-url}") String licenseUrl,
            @Value("${openapi.service.license-name}") String licenseName,
            @Value("${openapi.service.server-url}") String serverUrl,
            @Value("${openapi.service.server-description}") String serverDescription,
            @Value("${openapi.service.terms-of-service-url}") String termsOfServiceUrl
    ) {
        return new OpenAPI()
                .servers(List.of(new Server()
                        .url(serverUrl)
                        .description(serverDescription))
                )
                .info(new Info()
                        .title(title)
                        .version(version)
                        .description(description)
                        .contact(new Contact().email(contact))
                        .license(new License().name(licenseName).url(licenseUrl))
                        .termsOfService(termsOfServiceUrl)
                );
    }
}
