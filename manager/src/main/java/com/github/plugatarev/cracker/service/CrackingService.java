package com.github.plugatarev.cracker.service;

import com.github.plugatarev.cracker.common.RequestId;
import com.github.plugatarev.cracker.dto.CrackingRequest;
import com.github.plugatarev.cracker.dto.TaskStatus;
import com.github.plugatarev.cracker.common.WorkerCrackingResponse;
import com.github.plugatarev.cracker.exception.NotFoundException;

public interface CrackingService {

    RequestId submitCrackingRequest(CrackingRequest crackingRequest);

    TaskStatus getTaskStatus(RequestId requestId) throws NotFoundException;

    void updateTaskStatus(WorkerCrackingResponse workerResponse);
}
