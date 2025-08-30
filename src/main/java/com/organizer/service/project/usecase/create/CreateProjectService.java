package com.organizer.service.project.usecase.create;

import com.organizer.model.project.Project;
import com.organizer.model.project.ProjectName;
import com.organizer.service.project.port.ProjectStore;

import java.util.Objects;

public class CreateProjectService implements CreateProjectUseCase {

    private final ProjectStore projectStore;

    public CreateProjectService(ProjectStore projectStore) {
        Objects.requireNonNull(projectStore, "Project Store cannot be null");
        this.projectStore = projectStore;
    }

    @Override
    public CreateProjectResult handle(CreateProjectCommand command) {
        if (command == null) {
            return CreateProjectResult.Error.MISSING_COMMAND;
        }

        if (command.name() == null) {
            return CreateProjectResult.Error.MISSING_PROJECT_NAME;
        }

        ProjectName projectName;
        try {
            projectName = ProjectName.of(command.name());
        } catch (IllegalArgumentException e) {
            return CreateProjectResult.Error.INVALID_PROJECT_NAME_FORMAT;
        }

        Project project = new Project(projectName);
        projectStore.save(project);
        return new CreateProjectResult.Ok(project.getId());
    }
}
