package com.organizer.service.project.usecase.create;

import com.organizer.model.project.ProjectId;

public sealed interface CreateProjectResult {
    record Ok(ProjectId projectId) implements CreateProjectResult {
    }

    enum Error implements CreateProjectResult {
        MISSING_PROJECT_NAME, INVALID_PROJECT_NAME_FORMAT, MISSING_COMMAND

    }
}
