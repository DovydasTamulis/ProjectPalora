package com.pal;

import com.pal.config.EnvLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.pal.repository")
public class PaloraApplication {
    public static void main(String[] args) {
        EnvLoader.loadEnv();
        SpringApplication.run(PaloraApplication.class, args);
    }
}