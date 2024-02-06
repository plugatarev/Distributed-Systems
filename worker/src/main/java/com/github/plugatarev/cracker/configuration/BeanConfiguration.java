package com.github.plugatarev.cracker.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.Executor;

@Configuration
public class BeanConfiguration {

    @Bean
    public WebClient webClient() {
        return WebClient.create();
    }

    @Bean
    public Executor crackingTaskExecutor() {
        return new ThreadPoolTaskExecutor();
    }
}
