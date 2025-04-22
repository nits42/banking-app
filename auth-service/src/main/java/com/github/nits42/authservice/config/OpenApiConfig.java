package com.github.nits42.authservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@OpenAPIDefinition
@Configuration
public class OpenApiConfig {


    public OpenAPI customOpenAPI(
            @Value("${openapi.service.title}") String title,
            @Value("${openapi.service.version}") String version,
            @Value("${openapi.service.description}") String description,
            @Value("${openapi.service.contact}") String contact,
            @Value("${openapi.service.license}") String license,
            @Value("${openapi.service.url}") String url
    ) {
        return new OpenAPI()
                .servers(List.of(new Server().url(url)))
                .info(new Info()
                        .title(title)
                        .version(version)
                        .description(description)
                        .contact(new Contact().email(contact))
                        .license(new License().name(license))
                );
    }
}
