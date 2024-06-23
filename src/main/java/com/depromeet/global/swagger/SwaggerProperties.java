package com.depromeet.global.swagger;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "swagger")
public record SwaggerProperties(String version) {
}
