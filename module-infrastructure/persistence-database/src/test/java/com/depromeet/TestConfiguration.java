package com.depromeet;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.depromeet.*.repository")
@EntityScan(basePackages = "com.depromeet.*.entity")
public class TestConfiguration {}
