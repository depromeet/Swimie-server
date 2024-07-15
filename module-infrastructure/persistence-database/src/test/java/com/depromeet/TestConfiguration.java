package com.depromeet;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "com.depromeet.*.repository")
@EntityScan(basePackages = "com.depromeet.*.entity")
@SpringBootApplication
public class TestConfiguration {}
