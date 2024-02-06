package com.github.plugatarev.cracker.service;

import dto.WorkerCrackingRequest;
import dto.WorkerCrackingResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.paukov.combinatorics3.Generator;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultCrackingTaskService implements CrackingTaskService {

    private final WebClientResultSendingService sendingService;
    private final Executor crackingTaskExecutor;

    @Override
    public void executeCrackingTask(WorkerCrackingRequest managerRequest) {
        CompletableFuture.supplyAsync(() -> executeTask(managerRequest), crackingTaskExecutor)
                .thenAccept(sendingService::sendResultToManager);
    }

    public WorkerCrackingResponse executeTask(WorkerCrackingRequest managerRequest) {
        List<String> words = new ArrayList<>();
        for (int length = 1; length <= managerRequest.hashLength(); length++) {
            int allWordCount = (int) Math.pow(managerRequest.alphabet().size(), length);
            int start =
                    start(managerRequest.taskPartId(), managerRequest.workerCount(), allWordCount);
            int partWordCount =
                    currPartCount(
                            managerRequest.taskPartId(),
                            managerRequest.workerCount(),
                            allWordCount);
            words.addAll(
                    Generator.permutation(managerRequest.alphabet())
                            .withRepetitions(length)
                            .stream()
                            .skip(start)
                            .limit(partWordCount)
                            .map(word -> String.join("", word))
                            .filter(
                                    word ->
                                            managerRequest
                                                    .hash()
                                                    .equals(
                                                            DigestUtils.md5DigestAsHex(
                                                                    word.getBytes())))
                            .toList());
        }
        return new WorkerCrackingResponse(
                managerRequest.requestId(), managerRequest.taskPartId(), words);
    }

    private int start(int partNumber, int partCount, int words) {
        return (int) Math.ceil((double) words / partCount * partNumber);
    }

    private int currPartCount(int partNumber, int partCount, int words) {
        return (int)
                (Math.ceil((double) words / partCount * (partNumber + 1))
                        - Math.ceil((double) words / partCount * partNumber));
    }
}
