package com.organizer.service.task.usecase.delete;

public interface DeleteTaskUseCase {
    DeleteTaskResult handle(DeleteTaskCommand command);
}
