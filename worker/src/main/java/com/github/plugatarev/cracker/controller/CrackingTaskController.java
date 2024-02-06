package com.github.plugatarev.cracker.controller;

import com.github.plugatarev.cracker.service.CrackingTaskService;

import dto.WorkerCrackingRequest;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/internal/api/worker/hash/crack/task")
public class CrackingTaskController {

    private final CrackingTaskService crackingTaskService;

    @PostMapping
    public void executeCrackingTask(@RequestBody WorkerCrackingRequest managerRequest) {
        crackingTaskService.executeCrackingTask(managerRequest);
    }
}
