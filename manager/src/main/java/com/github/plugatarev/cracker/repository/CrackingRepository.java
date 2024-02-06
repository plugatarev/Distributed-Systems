package com.github.plugatarev.cracker.repository;

import com.github.plugatarev.cracker.dto.TaskStatus;

import dto.RequestId;

import java.util.Optional;

public interface CrackingRepository {

    void save(RequestId requestId, TaskStatus taskStatus);

    void update(RequestId requestId, TaskStatus taskStatus);

    Optional<TaskStatus> findById(RequestId id);
}
