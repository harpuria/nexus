package com.qwerty.nexus.global.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * SwaggerConfig
 * 스웨거 관련 설정
 *
 */
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        // JWT 를 header 에서 사용하기 위한 설정
        SecurityScheme jwtSecurityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        // 게잌 클라이언트 아이디를 header 에서 사용하기 위한 설정
        SecurityScheme xClientIdSecurityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name("X-CLIENT-ID");

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("bearerAuth")
                .addList("X-CLIENT-ID");

        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", jwtSecurityScheme)
                        .addSecuritySchemes("X-CLIENT-ID", xClientIdSecurityScheme))
                .addSecurityItem(securityRequirement)
                .info(new Info()
                        .title("Nexus API")
                        .version("v1")
                        .description("Nexus API Document")
                        .contact(new Contact()
                                .name("YHH")
                                .email("harpuria87@gmail.com")))
                .servers(List.of(new Server().url("http://localhost:9630").description("Local Server")));
    }
}
