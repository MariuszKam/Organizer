package com.organizer.service.task.usecase.create;

import com.organizer.service.task.usecase.create.command.CreateTaskCommand;

public interface CreateTaskUseCase {
    CreateTaskResult handle(CreateTaskCommand command);
}
