package com.github.plugatarev.cracker.service;

import com.github.plugatarev.cracker.exception.ResultSendingException;

import dto.WorkerCrackingResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class WebClientResultSendingService implements ResultSendingService {

    private final WebClient webClient;

    @Value("${manager.result-sending.url}")
    private String managerUrl;

    @Override
    public void sendResultToManager(WorkerCrackingResponse crackingDto) {
        webClient
                .patch()
                .uri(managerUrl)
                .bodyValue(crackingDto)
                .retrieve()
                .toBodilessEntity()
                .toFuture()
                .exceptionally(
                        throwable -> {
                            throw new ResultSendingException(throwable);
                        });
    }
}
