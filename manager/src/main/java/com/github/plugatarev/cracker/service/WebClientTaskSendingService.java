package com.github.plugatarev.cracker.service;

import com.github.plugatarev.cracker.dto.CrackingRequest;
import com.github.plugatarev.cracker.exception.TaskSendingException;

import dto.RequestId;
import dto.WorkerCrackingRequest;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

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
    public void sendTasksToWorkers(RequestId requestId, CrackingRequest requestDto) {
        for (int partIndex = 0; partIndex < workersCount; partIndex++) {
            final WorkerCrackingRequest submitTaskRequest =
                    new WorkerCrackingRequest(
                            requestId,
                            requestDto.hash(),
                            requestDto.maxLength(),
                            partIndex,
                            workersCount,
                            alphabet);

            webClient
                    .post()
                    .uri(workerUrl)
                    .bodyValue(submitTaskRequest)
                    .retrieve()
                    .toBodilessEntity()
                    .toFuture()
                    .exceptionally(
                            throwable -> {
                                throw new TaskSendingException(throwable);
                            });
        }
    }
}
