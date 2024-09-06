package com.depromeet.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "google.sheet")
public record SpreadSheetProperties(
        String credentialsFilePath,
        String applicationName,
        String sheetId,
        String range,
        String reportApplicationName,
        String reportSheetId,
        String reportRange) {}
