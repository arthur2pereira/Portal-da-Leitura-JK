package com.example.demo.integration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BooksConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

