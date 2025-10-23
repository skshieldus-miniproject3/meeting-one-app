package com.meetingoneline.meeting_one_line.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${ai.server.url}")
    private String aiServerUrl;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                        .baseUrl(aiServerUrl) // Base URL 설정
                        .build();
    }
}