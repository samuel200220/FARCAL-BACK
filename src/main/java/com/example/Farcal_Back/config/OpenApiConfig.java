package com.example.Farcal_Back.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Value("${app.openapi.server-url}")
    private String serverUrl;

    @Value("${app.openapi.server-description}")
    private String serverDescription;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addServersItem(new Server()
                        .url(serverUrl)
                        .description(serverDescription))
                .info(new Info()
                        .title("Fare Calculator API")
                        .version("1.0.0")
                        .description("API Spring Boot WebFlux pour l'authentification, le calcul et l'historique des trajets")
                        .contact(new Contact()
                                .name("YOWYOB")
                                .email("contact@farecalculator.com"))
                        .license(new License()
                                .name("Apache 2.0")));
    }
}
