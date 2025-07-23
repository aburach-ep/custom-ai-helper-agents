package com.epam.customaijira.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Value("${jira.api-token}")
    private String jiraApiToken;

    @Value("${dial.api-key}")
    private String dialApiKey;

    @Bean
    public RequestInterceptor jiraRequestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Authorization", "Bearer " + jiraApiToken);
            requestTemplate.header("Accept", "application/json");
        };
    }

    @Bean
    public RequestInterceptor dialRequestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("api-key", dialApiKey);
            requestTemplate.header("Content-Type", "application/json");
        };
    }
} 