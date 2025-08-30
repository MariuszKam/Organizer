package com.organizer.service.project.usecase.update;

import com.organizer.model.project.ProjectId;

public sealed interface UpdateProjectResult {
    record Ok(ProjectId projectId) implements UpdateProjectResult {
    }

    enum Error implements UpdateProjectResult {

    }
}
