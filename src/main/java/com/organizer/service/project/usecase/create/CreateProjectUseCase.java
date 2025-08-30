package com.organizer.service.project.usecase.create;

public interface CreateProjectUseCase {
    CreateProjectResult handle(CreateProjectCommand command);
}
