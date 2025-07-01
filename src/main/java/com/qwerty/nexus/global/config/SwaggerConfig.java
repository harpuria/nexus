package com.qwerty.nexus.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
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
        return new OpenAPI()
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
