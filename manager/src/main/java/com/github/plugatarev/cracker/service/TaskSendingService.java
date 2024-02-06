package com.github.plugatarev.cracker.service;

import com.github.plugatarev.cracker.common.RequestId;
import com.github.plugatarev.cracker.dto.CrackingRequest;

public interface TaskSendingService {
        void sendTasksToWorkers(RequestId requestId, CrackingRequest requestDto);
}
