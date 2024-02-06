package com.github.plugatarev.cracker.controller;

import com.github.plugatarev.cracker.dto.CrackingRequest;
import com.github.plugatarev.cracker.dto.TaskStatus;
import com.github.plugatarev.cracker.exception.NotFoundTaskException;
import com.github.plugatarev.cracker.service.DefaultCrackingService;

import dto.RequestId;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hash/")
@RequiredArgsConstructor
public class ManagerExternalController {

    private final DefaultCrackingService crackingService;

    @PostMapping("/crack")
    public ResponseEntity<RequestId> crackHash(
            @Valid @RequestBody CrackingRequest creationRequest) {
        RequestId requestId = crackingService.submitCrackingRequest(creationRequest);
        return ResponseEntity.ok(requestId);
    }

    @GetMapping("/status")
    public ResponseEntity<TaskStatus> getTaskStatus(@RequestParam RequestId requestId)
            throws NotFoundTaskException {
        TaskStatus statusTask = crackingService.getTaskStatus(requestId);
        return ResponseEntity.ok(statusTask);
    }
}
