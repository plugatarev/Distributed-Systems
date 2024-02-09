package com.github.plugatarev.cracker.service;

import com.github.plugatarev.cracker.dto.CrackingRequest;

import dto.RequestId;

import java.util.concurrent.CompletableFuture;

public interface TaskSendingService {
    CompletableFuture<Void> sendTasksToWorkers(RequestId requestId, CrackingRequest requestDto);
}
