package com.github.plugatarev.cracker.controller;

import com.github.plugatarev.cracker.common.WorkerCrackingResponse;
import com.github.plugatarev.cracker.service.CrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/internal/api/manager/hash/crack/request")
public class ManagerInternalController {

    private final CrackingService crackingRequestService;

    @PatchMapping
    public void updateCrackingRequestResultsBy(@RequestBody WorkerCrackingResponse workerResponse) {
        crackingRequestService.updateTaskStatus(workerResponse);
    }

}