package com.organizer.service.task.usecase.read;

public interface ReadTaskUseCase {
    ReadTaskResult handle(ReadTaskCommand command);
}
