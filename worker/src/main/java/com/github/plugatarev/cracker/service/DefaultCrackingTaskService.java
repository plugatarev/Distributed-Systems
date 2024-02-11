package com.github.plugatarev.cracker.service;

import dto.WorkerCrackingRequest;
import dto.WorkerCrackingResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.paukov.combinatorics3.Generator;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
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
        log.info("Received cracking task from manager for id='{}'", managerRequest.id().requestId());
        CompletableFuture.supplyAsync(() -> executeTask(managerRequest), crackingTaskExecutor)
                .thenAccept(sendingService::sendResultToManager);
    }

    private WorkerCrackingResponse executeTask(WorkerCrackingRequest managerRequest) {
        log.info("Start execution task='{}' for part='{}'", managerRequest.id().requestId(), managerRequest.taskPartId());
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
                                    word -> {
                                        String current =
                                                DigestUtils.md5DigestAsHex(
                                                        word.getBytes(StandardCharsets.UTF_8));
                                        if (managerRequest.hash().equals(current)) {
                                            log.info(
                                                    "New result='{}' for hash='{}' with length='{}'",
                                                    current,
                                                    managerRequest.hash(),
                                                    managerRequest.hashLength());
                                            return true;
                                        }
                                        return false;
                                    })
                            .toList());
        }
        log.info("Finished execution task, result='{}'", words);
        return new WorkerCrackingResponse(
                managerRequest.id(), managerRequest.taskPartId(), words);
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
