package com.github.plugatarev.cracker.repository;

import com.github.plugatarev.cracker.dto.TaskStatus;
import com.github.plugatarev.cracker.entity.TaskStatusEntity;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CrackingTaskRepository extends MongoRepository<TaskStatusEntity, String> {

    List<TaskStatusEntity> findByStatus(TaskStatus.Stage status);
}
