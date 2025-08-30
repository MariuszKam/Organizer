package com.organizer.service.task.usecase.create;

import com.organizer.model.task.*;
import com.organizer.model.user.User;
import com.organizer.model.user.Username;
import com.organizer.service.task.port.GeneratorId;
import com.organizer.service.task.port.TaskStore;
import com.organizer.service.task.usecase.create.command.CreateBasicTaskCommand;
import com.organizer.service.task.usecase.create.command.CreateFullTaskCommand;
import com.organizer.service.task.usecase.create.command.CreateTaskCommand;
import com.organizer.service.user.port.UserStore;
import com.organizer.service.user.usecase.create.parser.BasicResult;
import com.organizer.service.user.usecase.create.parser.ResultParser;

import java.util.Objects;

public class CreateTaskService implements CreateTaskUseCase {

    private final TaskStore taskStore;
    private final UserStore userStore;
    private final GeneratorId generatorId;

    public CreateTaskService(TaskStore taskStore, UserStore userStore, GeneratorId generatorId) {
        Objects.requireNonNull(taskStore, "Task store cannot be null");
        this.taskStore = taskStore;
        Objects.requireNonNull(userStore, "User store cannot be null");
        this.userStore = userStore;
        Objects.requireNonNull(generatorId, "Generator ID cannot be null");
        this.generatorId = generatorId;
    }

    @Override
    public CreateTaskResult handle(CreateTaskCommand command) {
        if (command == null) {
            return CreateTaskResult.Error.MISSING_COMMAND;
        }

        return switch (command) {
            case CreateFullTaskCommand fullTaskCommand -> handleFullTaskCommand(fullTaskCommand.name(),
                    fullTaskCommand.description(),
                    fullTaskCommand.priority(),
                    fullTaskCommand.status(),
                    fullTaskCommand.username());
            case CreateBasicTaskCommand basicTaskCommand ->
                    handleBasicTaskCommand(basicTaskCommand.name(), basicTaskCommand.description());
        };
    }

    private CreateTaskResult handleFullTaskCommand(String name, String description, String priority, String status, String username) {
        ResultParser resultParser = parseBasic(name, description);
        if (resultParser instanceof ResultParser.Err(CreateTaskResult.Error error)) {
            return error;
        }

        BasicResult result = ((ResultParser.Ok<BasicResult>) resultParser).value();

        if (priority == null) {
            return CreateTaskResult.Error.MISSING_TASK_PRIORITY;
        }

        TaskPriority taskPriority;
        try {
            taskPriority = TaskPriority.valueOf(priority);
        } catch (IllegalArgumentException e) {
            return CreateTaskResult.Error.INVALID_TASK_PRIORITY_NAME;
        }

        if (status == null) {
            return CreateTaskResult.Error.MISSING_TASK_STATUS;
        }

        TaskStatus taskStatus;
        try {
            taskStatus = TaskStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            return CreateTaskResult.Error.INVALID_TASK_STATUS_NAME;
        }

        if (username == null) {
            return CreateTaskResult.Error.MISSING_USERNAME;
        }

        Username userUsername;
        try {
            userUsername = Username.of(username);
        } catch (IllegalArgumentException e) {
            return CreateTaskResult.Error.INVALID_USERNAME;
        }

        User taskUser = userStore.findByUsername(userUsername).orElse(null);
        if (taskUser == null) {
            return CreateTaskResult.Error.NON_EXISTING_USER;
        }
        TaskId id = generatorId.generateId();

        Task task = new Task(id, result.name(), result.taskDescription(), taskPriority, taskStatus, taskUser);
        taskStore.save(task);
        return new CreateTaskResult.Ok(id);
    }

    private CreateTaskResult handleBasicTaskCommand(String name, String description) {
        var basicResult = parseBasic(name, description);
        if (basicResult instanceof ResultParser.Err(CreateTaskResult.Error error)) {
            return error;
        }

        BasicResult result = ((ResultParser.Ok<BasicResult>) basicResult).value();

        TaskId id = generatorId.generateId();
        Task task = new Task(result.name(), result.taskDescription());

        taskStore.save(task);
        return new CreateTaskResult.Ok(id);
    }

    private ResultParser parseBasic(String name, String description) {
        if (name == null) {
            return new ResultParser.Err(CreateTaskResult.Error.MISSING_TASK_NAME);
        }

        if (description == null) {
            return new ResultParser.Err(CreateTaskResult.Error.MISSING_TASK_DESCRIPTION);
        }

        TaskName taskName;
        try {
            taskName = TaskName.of(name);
        } catch (IllegalArgumentException e) {
            return new ResultParser.Err(CreateTaskResult.Error.INVALID_TASK_NAME_FORMAT);
        }

        TaskDescription taskDescription;
        try {
            taskDescription = TaskDescription.of(description);
        } catch (IllegalArgumentException e) {
            return new ResultParser.Err(CreateTaskResult.Error.INVALID_TASK_DESCRIPTION_FORMAT);
        }

        return new ResultParser.Ok<>(new BasicResult(taskName, taskDescription));
    }
}
