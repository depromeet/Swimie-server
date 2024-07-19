package com.depromeet;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.depromeet.*.entity")
@EnableJpaRepositories(basePackages = "com.depromeet.*.repository")
public class TestConfiguration {}
