package com.github.plugatarev.cracker.repository;

import com.github.plugatarev.cracker.common.RequestId;
import com.github.plugatarev.cracker.dto.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@RequiredArgsConstructor
public class InMemoryCrackingRepository implements CrackingRepository {

    private final ConcurrentHashMap<RequestId, TaskStatus> requests;

    @Override
    public void save(RequestId requestId, TaskStatus taskStatus) {
        requests.put(requestId, taskStatus);
    }

    @Override
    public void update(RequestId requestId, TaskStatus taskStatus) {
        requests.put(requestId, taskStatus);
    }

    @Override
    public Optional<TaskStatus> findById(RequestId id) {
        TaskStatus taskStatus = requests.get(id);
        return taskStatus == null ? Optional.empty() : Optional.of(taskStatus);
    }
}
