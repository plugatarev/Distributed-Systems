package com.github.plugatarev.cracker.service;

import dto.WorkerCrackingRequest;

public interface CrackingTaskService {

    void executeCrackingTask(WorkerCrackingRequest managerRequest);
}
