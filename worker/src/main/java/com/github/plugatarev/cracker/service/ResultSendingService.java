package com.github.plugatarev.cracker.service;

import com.github.plugatarev.cracker.common.WorkerCrackingResponse;

public interface ResultSendingService {
        void sendResultToManager(WorkerCrackingResponse crackingDto);
}
