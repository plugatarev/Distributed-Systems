package com.github.plugatarev.cracker.service;

import com.github.plugatarev.cracker.dto.CrackingRequest;
import com.github.plugatarev.cracker.dto.TaskStatus;
import com.github.plugatarev.cracker.exception.NotFoundTaskException;

import dto.RequestId;
import dto.WorkerCrackingResponse;

public interface CrackingService {

    RequestId submitCrackingRequest(CrackingRequest crackingRequest);

    TaskStatus getTaskStatus(RequestId requestId) throws NotFoundTaskException;

    void updateTaskStatus(WorkerCrackingResponse workerResponse);
}
