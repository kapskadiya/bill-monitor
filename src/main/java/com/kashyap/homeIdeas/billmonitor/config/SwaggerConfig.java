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
    public GroupedOpenApi userApiGroup() {
        return GroupedOpenApi
                .builder()
                .group("user")
                .pathsToMatch("/**/user/**")
                .build();
    }

    @Bean
    public GroupedOpenApi authApiGroup() {
        return GroupedOpenApi
                .builder()
                .group("auth")
                .pathsToMatch("/**/auth/**")
                .build();
    }

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
                            .title("Bill monitor apis")
                            .description("Rest apis for bill monitor app")
                            .version("1.0")
                );
    }

}
