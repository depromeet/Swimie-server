package com.depromeet.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@RequiredArgsConstructor
public class AWSS3Config {
    private final AwsS3Properties awsProperties;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .credentialsProvider(
                        () ->
                                AwsBasicCredentials.create(
                                        awsProperties.accessKey(), awsProperties.secretKey()))
                .region(Region.AP_NORTHEAST_2)
                .build();
    }
}
