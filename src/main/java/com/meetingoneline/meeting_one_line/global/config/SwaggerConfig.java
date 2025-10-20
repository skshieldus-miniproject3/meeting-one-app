package com.meetingoneline.meeting_one_line.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI meetingOneLineOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Meeting One Line API Docs")
                        .description("AI 회의록 관리 시스템 API 명세서")
                        .version("1.0.0"));
    }
}