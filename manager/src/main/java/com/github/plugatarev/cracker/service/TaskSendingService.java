package com.github.plugatarev.cracker.service;

import com.github.plugatarev.cracker.dto.CrackingRequest;

import dto.RequestId;

public interface TaskSendingService {
    void sendTasksToWorkers(RequestId requestId, CrackingRequest requestDto);
}
