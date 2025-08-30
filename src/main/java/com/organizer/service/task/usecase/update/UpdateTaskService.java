package com.organizer.service.task.usecase.update;

import com.organizer.model.task.*;
import com.organizer.model.user.User;
import com.organizer.model.user.Username;
import com.organizer.service.task.port.TaskStore;
import com.organizer.service.user.port.UserStore;

import java.util.Objects;

public class UpdateTaskService implements UpdateTaskUseCase {

    private final TaskStore taskStore;
    private final UserStore userStore;

    public UpdateTaskService(TaskStore taskStore, UserStore userStore) {
        Objects.requireNonNull(taskStore, "Task store cannot be null");
        this.taskStore = taskStore;
        Objects.requireNonNull(userStore, "User store cannot be null");
        this.userStore = userStore;
    }

    @Override
    public UpdateTaskResult handle(UpdateTaskCommand command) {
        if (command == null) {
            return UpdateTaskResult.Error.MISSING_COMMAND;
        }

        if (command.taskId() == null) {
            return UpdateTaskResult.Error.MISSING_TASK_ID;
        }

        if (command.name().isEmpty() &&
                command.description().isEmpty() &&
                command.priority().isEmpty() &&
                command.status().isEmpty() &&
                command.username().isEmpty()) {
            return UpdateTaskResult.Error.NO_FIELDS_PROVIDED;
        }

        TaskId taskId;
        try {
            taskId = TaskId.of(command.taskId());
        } catch (IllegalArgumentException e) {
            return UpdateTaskResult.Error.INVALID_TASK_ID_FORMAT;
        }

        Task currentTask = taskStore.findById(taskId).orElse(null);
        if (currentTask == null) {
            return UpdateTaskResult.Error.NON_EXISTING_TASK;
        }

        TaskName taskName;
        if (command.name().isPresent()) {
            try {
                taskName = TaskName.of(command.name().get());
            } catch (IllegalArgumentException e) {
                return UpdateTaskResult.Error.INVALID_TASK_NAME_FORMAT;
            }
        } else {
            taskName = currentTask.getName();
        }

        TaskDescription taskDescription;
        if (command.description().isPresent()) {
            try {
                taskDescription = TaskDescription.of(command.description().get());
            } catch (IllegalArgumentException e) {
                return UpdateTaskResult.Error.INVALID_TASK_DESCRIPTION_FORMAT;
            }
        } else {
            taskDescription = currentTask.getDescription();
        }

        TaskPriority taskPriority;
        if (command.priority().isPresent()) {
            try {
                taskPriority = TaskPriority.valueOf(command.priority().get());
            } catch (IllegalArgumentException e) {
                return UpdateTaskResult.Error.INVALID_TASK_PRIORITY_FORMAT;
            }
        } else {
            taskPriority = currentTask.getPriority();
        }

        TaskStatus taskStatus;
        if (command.status().isPresent()) {
            try {
                taskStatus = TaskStatus.valueOf(command.status().get());
            } catch (IllegalArgumentException e) {
                return UpdateTaskResult.Error.INVALID_TASK_STATUS_FORMAT;
            }
        } else {
            taskStatus = currentTask.getStatus();
        }

        User user;
        if (command.username().isPresent()) {
            Username username;
            try {
                username = Username.of(command.username().get());
            } catch (IllegalArgumentException e) {
                return UpdateTaskResult.Error.INVALID_USERNAME_FORMAT;
            }
            user = userStore.findByUsername(username).orElse(null);
            if (user == null) {
                return UpdateTaskResult.Error.NON_EXISTING_USER;
            }
        } else {
            user = currentTask.getAssignedUser();
        }

        Task updatedTask = new Task(taskId, taskName, taskDescription, taskPriority, taskStatus, user);
        taskStore.save(updatedTask);
        return new UpdateTaskResult.Ok(taskId);

    }

}
