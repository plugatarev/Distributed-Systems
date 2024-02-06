package com.github.plugatarev.cracker.service;

import com.github.plugatarev.cracker.common.WorkerCrackingRequest;

public interface CrackingTaskService {

    void executeCrackingTask(WorkerCrackingRequest managerRequest);

}