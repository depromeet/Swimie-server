package com.depromeet.global.properties;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationPropertiesScan(basePackages = "com.depromeet")
public class ScanningPropertiesConfiguration {

}
