package com.awesome.userservice.config;

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
                        .title("User Service API")
                        .version("1.0.0-SNAPSHOT") //Todo version should be dynamic
                        .description("This API manages user operations such as registration, authentication, and password changes.")
                        .contact(new Contact()
                                .name("Awesome Support Team")
                                .email("support@awesome.com")
                                .url("https://awesome.com")));
    }
}

