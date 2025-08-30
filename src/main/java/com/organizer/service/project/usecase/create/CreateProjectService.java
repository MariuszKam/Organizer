package com.organizer.service.project.usecase.create;

import com.organizer.model.project.Project;
import com.organizer.model.project.ProjectId;
import com.organizer.model.project.ProjectName;
import com.organizer.service.project.port.GeneratorId;
import com.organizer.service.project.port.ProjectStore;

import java.util.Objects;

public class CreateProjectService implements CreateProjectUseCase {

    private final ProjectStore projectStore;
    private final GeneratorId generatorId;

    public CreateProjectService(ProjectStore projectStore, GeneratorId generatorId) {
        Objects.requireNonNull(projectStore, "Project Store cannot be null");
        this.projectStore = projectStore;
        Objects.requireNonNull(generatorId, "Generator ID cannot be null");
        this.generatorId = generatorId;
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
        ProjectId projectId = generatorId.generate();
        Project project = new Project(projectId, projectName);
        projectStore.save(project);
        return new CreateProjectResult.Ok(project.getId());
    }
}
