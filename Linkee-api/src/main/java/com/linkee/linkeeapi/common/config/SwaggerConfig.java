package com.linkee.linkeeapi.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Linkee API 문서")
                        .description("CS 지식 퀴즈 플랫폼 Linkee의 REST API 문서입니다.")
                        .version("v1.0.0"));
    }
}
