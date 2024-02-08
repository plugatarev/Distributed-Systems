package com.github.plugatarev.cracker.service;

import com.github.plugatarev.cracker.producer.CrackingTaskResultProducer;

import dto.WorkerCrackingResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitSendingService implements ResultSendingService {

    private final CrackingTaskResultProducer taskResultProducer;

    @Override
    public void sendResultToManager(WorkerCrackingResponse crackingDto) {
        taskResultProducer.produce(crackingDto);
    }
}
