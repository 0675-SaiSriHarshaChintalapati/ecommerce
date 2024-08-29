package com.digit.ecommerce.config;


import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.digit.ecommerce.repository")
@EntityScan(basePackages = "com.digit.ecommerce.model")
public class JpaConfig {
}
