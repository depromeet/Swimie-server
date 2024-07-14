package com.depromeet.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.cloud.aws.credentials")
public record AwsS3Properties(String accessKey, String secretKey) {}
