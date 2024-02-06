package com.github.plugatarev.cracker.service;

import dto.WorkerCrackingResponse;

public interface ResultSendingService {
    void sendResultToManager(WorkerCrackingResponse crackingDto);
}
