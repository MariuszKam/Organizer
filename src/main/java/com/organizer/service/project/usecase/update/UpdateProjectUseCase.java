package com.organizer.service.project.usecase.update;

import com.organizer.service.project.usecase.update.command.UpdateProjectCommand;

public interface UpdateProjectUseCase {
    UpdateProjectResult handle(UpdateProjectCommand command);
}
