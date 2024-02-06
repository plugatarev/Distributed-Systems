package com.github.plugatarev.cracker.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@JsonIgnoreProperties("startTime")
public class TaskStatus {

    @Getter @Setter
    private Stage taskStage;
    private List<String> data;
    private Set<Integer> completedTasks;
    @Getter
    private Instant startTime;

    public TaskStatus() {
        this.taskStage = Stage.IN_PROGRESS;
        this.data = new ArrayList<>();
            this.completedTasks = new HashSet<>();
        this.startTime = Instant.now();
    }

    public void completeTask(int workerPart, List<String> data) {
        if (completedTasks.add(workerPart)) {
            this.data.addAll(data);
        }
    }

    public int completedTasks() {
        return completedTasks.size();
    }

    public enum Stage {
        IN_PROGRESS,
        READY,
        ERROR
    }
}
