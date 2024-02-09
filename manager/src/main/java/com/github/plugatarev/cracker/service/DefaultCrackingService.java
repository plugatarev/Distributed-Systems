package com.github.plugatarev.cracker.service;

import com.github.plugatarev.cracker.dto.CrackingRequest;
import com.github.plugatarev.cracker.dto.TaskStatus;
import com.github.plugatarev.cracker.exception.NotFoundTaskException;
import com.github.plugatarev.cracker.exception.TaskSendingException;
import com.github.plugatarev.cracker.repository.CrackingRepository;

import dto.RequestId;
import dto.WorkerCrackingResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultCrackingService implements CrackingService {

    private final CrackingRepository crackingRepository;
    private final TaskSendingService taskSendingService;

    @Value("${cracking-request.timeout}")
    private Duration timeout;

    @Value("${workers.number}")
    private int workersCount;

    @Override
    public RequestId submitCrackingRequest(CrackingRequest crackingRequest) {
        log.info("Received cracking request from user, hash='{}' and maxLength='{}'", crackingRequest.hash(), crackingRequest.maxLength());
        RequestId requestId = new RequestId(UUID.randomUUID());
        TaskStatus taskStatus = new TaskStatus();

        crackingRepository.save(requestId, taskStatus);
        CompletableFuture<Void> sendStatus = taskSendingService.sendTasksToWorkers(requestId, crackingRequest);
        sendStatus.exceptionally(ex -> {
            log.info("Sending task='{}' error", requestId.requestId());
            taskStatus.setStatus(TaskStatus.Stage.ERROR);
            crackingRepository.update(requestId, taskStatus);
            throw new TaskSendingException(ex);
        });

        return requestId;
    }

    @Override
    public synchronized TaskStatus getTaskStatus(RequestId id) throws NotFoundTaskException {
        log.info("Received status request from user for task='{}'", id.requestId());
        TaskStatus taskStatus =
                crackingRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new NotFoundTaskException(
                                                "Request not found: " + id));
        Duration dur = Duration.between(taskStatus.getStartTime(), Instant.now());
        if (dur.toMillis() > timeout.toMillis()) {
            log.info("Task='{}' timeout", id);
            taskStatus.setStatus(TaskStatus.Stage.ERROR);
        }
        return taskStatus;
    }

    @Override
    public synchronized void updateTaskStatus(WorkerCrackingResponse workerResponse) {
        RequestId requestId = workerResponse.id();
        TaskStatus status = crackingRepository.findById(requestId).orElseThrow();
        log.info("Received update request from worker for task='{}', workerPart='{}', data='{}'", requestId.requestId(), workerResponse.workerPart(), workerResponse.data());

        status.completeTask(workerResponse.workerPart(), workerResponse.data());
        if (status.completedTasks() == workersCount
                && status.getStatus().equals(TaskStatus.Stage.IN_PROGRESS)) {
            log.info("Task='{}' ready", requestId);
            status.setStatus(TaskStatus.Stage.READY);
            crackingRepository.update(requestId, status);
        }
    }
}
