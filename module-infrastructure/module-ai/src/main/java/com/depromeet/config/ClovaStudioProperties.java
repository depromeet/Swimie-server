package com.depromeet.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "clova")
public record ClovaStudioProperties(
        String baseUrl,
        String summaryApi,
        String chatCompletionsApi,
        String X_NCP_CLOVASTUDIO_API_KEY,
        String X_NCP_APIGW_API_KEY) {}
