package com.github.plugatarev.cracker.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Configuration
public class BeanConfiguration {

    @Bean
    public WebClient webClient() {
        return WebClient.create();
    }

    @Bean
    public List<String> alphabet() {
        return List.of("abcdefghijklmnopqrstuvwxyz0123456789".split(""));
    }
}
