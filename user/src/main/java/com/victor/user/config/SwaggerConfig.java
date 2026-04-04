package com.victor.user.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("User Microservice API")
                        .description("Microservice responsible for user management and email publishing")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Victor Marques")
                                .email("Victor.Oliveira.98v@gmail.com")
                                .url("https://github.com/Ivictors")));
    }
}
