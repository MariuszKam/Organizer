package com.organizer.service.task.usecase.update;

public interface UpdateTaskUseCase {
    UpdateTaskResult handle(UpdateTaskCommand command);
}
