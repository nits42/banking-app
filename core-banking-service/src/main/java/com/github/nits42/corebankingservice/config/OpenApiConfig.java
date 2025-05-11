package com.github.nits42.corebankingservice.config;

import com.github.nits42.corebankingservice.util.AppConstant;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
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
                )
                .addSecurityItem(new SecurityRequirement().addList(AppConstant.BEARER_AUTH))
                .components(new Components().addSecuritySchemes(
                        AppConstant.BEARER_AUTH,
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme(AppConstant.BEARER)
                                .bearerFormat(AppConstant.ISSUER)
                                .in(SecurityScheme.In.HEADER)
                                .name(AppConstant.AUTHORIZATION)
                ));
    }
}
