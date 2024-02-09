package com.github.plugatarev.cracker.service;

import com.github.plugatarev.cracker.dto.CrackingRequest;

import dto.RequestId;
import dto.WorkerCrackingRequest;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class WebClientTaskSendingService implements TaskSendingService {

    private final WebClient webClient;
    private final List<String> alphabet;

    @Value("${worker.task-sending.url}")
    private String workerUrl;

    @Value("${workers.number}")
    private int workersCount;

    @Override
    public CompletableFuture<Void> sendTasksToWorkers(RequestId requestId, CrackingRequest requestDto) {
        var responses = new ArrayList<CompletableFuture<ResponseEntity<Void>>>();
        for (int partIndex = 0; partIndex < workersCount; partIndex++) {
            final WorkerCrackingRequest submitTaskRequest =
                    new WorkerCrackingRequest(
                            requestId,
                            requestDto.hash(),
                            requestDto.maxLength(),
                            partIndex,
                            workersCount,
                            alphabet);


            var response = webClient
                    .post()
                    .uri(workerUrl)
                    .bodyValue(submitTaskRequest)
                    .retrieve()
                    .toBodilessEntity()
                    .toFuture();
            responses.add(response);
        }
        return CompletableFuture.allOf(responses.toArray(new CompletableFuture[0]));
    }
}
