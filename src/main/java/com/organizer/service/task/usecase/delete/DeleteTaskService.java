package com.organizer.service.task.usecase.delete;

import com.organizer.model.task.Task;
import com.organizer.model.task.TaskId;
import com.organizer.service.task.port.TaskStore;

import java.util.Objects;

public class DeleteTaskService implements DeleteTaskUseCase {

    private final TaskStore taskStore;

    public DeleteTaskService(TaskStore taskStore) {
        Objects.requireNonNull(taskStore, "Task store cannot be null");
        this.taskStore = taskStore;
    }

    @Override
    public DeleteTaskResult handle(DeleteTaskCommand command) {
        if (command == null) {
            return DeleteTaskResult.Error.MISSING_COMMAND;
        }

        if (command.id() == null) {
            return DeleteTaskResult.Error.MISSING_TASK_ID;
        }

        TaskId taskId;
        try {
            taskId = TaskId.of(command.id());
        } catch (IllegalArgumentException e) {
            return DeleteTaskResult.Error.INVALID_TASK_ID_FORMAT;
        }
        Task task = taskStore.findById(taskId).orElse(null);

        if (task == null) {
            return DeleteTaskResult.Error.NON_EXISTING_TASK;
        }

        taskStore.remove(task);
        return new DeleteTaskResult.Ok(task.getId());
    }
}
