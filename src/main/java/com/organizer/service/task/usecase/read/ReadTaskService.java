package com.organizer.service.task.usecase.read;

import com.organizer.model.task.Task;
import com.organizer.model.task.TaskId;
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
        if (command == null) {
            return ReadTaskResult.Error.MISSING_COMMAND;
        }

        if (command.id() == null) {
            return ReadTaskResult.Error.MISSING_TASK_ID;
        }

        TaskId taskId;
        try {
            taskId = TaskId.of(command.id());
        } catch (IllegalArgumentException e) {
            return ReadTaskResult.Error.INVALID_TASK_ID_FORMAT;
        }

        Task task = taskStore.findById(taskId).orElse(null);
        if (task == null) {
            return ReadTaskResult.Error.NON_EXISTING_TASK;
        }
        return new ReadTaskResult.Ok(task);
    }
}
