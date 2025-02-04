package com.pal.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class AppConfig {

    @Bean
    public CommandLineRunner initTTLIndex(MongoTemplate mongoTemplate, MongoConfig mongoConfig) {
        return args -> {
            mongoConfig.createTTLIndex(mongoTemplate);
        };
    }
}