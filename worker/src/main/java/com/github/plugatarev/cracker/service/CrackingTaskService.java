package com.github.plugatarev.cracker.service;

import dto.WorkerCrackingRequest;
import dto.WorkerCrackingResponse;

import java.util.concurrent.CompletableFuture;

public interface CrackingTaskService {

    CompletableFuture<WorkerCrackingResponse> executeCrackingTask(
            WorkerCrackingRequest managerRequest);
}
