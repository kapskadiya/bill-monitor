package com.kashyap.homeIdeas.billmonitor.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI apiInfo() {
        final String securitySchemaName = "bearerAuth";

        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(securitySchemaName))
                .components(
                        new Components()
                            .addSecuritySchemes(securitySchemaName,
                                    new SecurityScheme()
                                        .name(securitySchemaName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                            )
                )
                .info(
                        new Info()
                            .title("Bill Monitor REST APIs")
                            .description("Rest apis for bill monitor app")
                            .version("1.0")
                );
    }

    @Bean
    GroupedOpenApi userApis() {
        return GroupedOpenApi.builder().group("User").pathsToMatch("/**/user/**").build();
    }

    @Bean
    GroupedOpenApi adminApis() {
        return GroupedOpenApi.builder().group("Authentication").pathsToMatch("/**/auth/**").build();
    }

    @Bean
    GroupedOpenApi billApis() {
        return GroupedOpenApi.builder().group("Bill").pathsToMatch("/**/bill/**").build();
    }

    @Bean
    GroupedOpenApi analyticsApis() {
        return GroupedOpenApi.builder().group("Analytics").pathsToMatch("/**/analytics/**").build();
    }

}
