package com.organizer.service.task.usecase.read;

import com.organizer.service.task.port.TaskStore;

import java.util.Objects;

public class ReadTaskService implements ReadTaskUseCase {

    private final TaskStore taskStore;

    public ReadTaskService(TaskStore taskStore) {
        Objects.requireNonNull(taskStore, "Task store cannot be null");
        this.taskStore = taskStore;
    }

    @Override
    public ReadTaskResult handle(ReadTaskCommand command) {
        return null;
    }
}
