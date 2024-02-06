package com.github.plugatarev.cracker.service;

import com.github.plugatarev.cracker.dto.CrackingRequest;
import com.github.plugatarev.cracker.dto.TaskStatus;
import com.github.plugatarev.cracker.exception.NotFoundTaskException;
import com.github.plugatarev.cracker.repository.CrackingRepository;

import dto.RequestId;
import dto.WorkerCrackingResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DefaultCrackingService implements CrackingService {

    private final CrackingRepository crackingRepository;
    private final TaskSendingService taskSendingService;

    @Value("${cracking-request.timeout}")
    private Duration timeout;

    @Value("${workers.number}")
    private int workersCount;

    @Override
    public RequestId submitCrackingRequest(CrackingRequest crackingRequest) {
        RequestId requestId = new RequestId(UUID.randomUUID());
        TaskStatus taskStatus = new TaskStatus();

        crackingRepository.save(requestId, taskStatus);
        taskSendingService.sendTasksToWorkers(requestId, crackingRequest);

        return requestId;
    }

    @Override
    public TaskStatus getTaskStatus(RequestId id) throws NotFoundTaskException {
        TaskStatus taskStatus =
                crackingRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new NotFoundTaskException(
                                                "Request not found: " + id.toString()));
        Duration dur = Duration.between(taskStatus.getStartTime(), Instant.now());
        if (dur.toMillis() > timeout.toMillis()) {
            taskStatus.setStatus(TaskStatus.Stage.ERROR);
        }
        return taskStatus;
    }

    @Override
    public synchronized void updateTaskStatus(WorkerCrackingResponse workerResponse) {
        RequestId requestId = workerResponse.id();
        TaskStatus status = crackingRepository.findById(requestId).orElseThrow();

        status.completeTask(workerResponse.workerPart(), workerResponse.data());
        if (status.completedTasks() == workersCount
                && status.getStatus().equals(TaskStatus.Stage.IN_PROGRESS)) {
            status.setStatus(TaskStatus.Stage.READY);
            crackingRepository.update(requestId, status);
        }
    }
}
