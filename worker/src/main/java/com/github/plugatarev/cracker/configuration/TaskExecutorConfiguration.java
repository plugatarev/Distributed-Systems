package com.github.plugatarev.cracker.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class TaskExecutorConfiguration {

    @Value("${thread-pool.task-execute.size}")
    private Integer threadPoolTaskExecuteSize;

    @Bean
    public Executor crackingTaskExecutor() {
        return Executors.newFixedThreadPool(threadPoolTaskExecuteSize);
    }
}
