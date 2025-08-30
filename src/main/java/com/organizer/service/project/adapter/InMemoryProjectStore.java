package com.organizer.service.project.adapter;

import com.organizer.model.project.Project;
import com.organizer.model.project.ProjectId;
import com.organizer.service.project.port.ProjectStore;

import java.util.*;

public final class InMemoryProjectStore implements ProjectStore {

    private final Map<ProjectId, Project> projectById;

    public InMemoryProjectStore() {
        this.projectById = new HashMap<>();
    }

    @Override
    public void save(Project project) {
        Objects.requireNonNull(project, "Project cannot be null");
        projectById.put(project.getId(), project);
    }

    @Override
    public void remove(Project project) {
        Objects.requireNonNull(project, "Project cannot be null");
        projectById.remove(project.getId());
    }

    @Override
    public Optional<Project> findById(ProjectId projectId) {
        return Optional.ofNullable(projectById.get(projectId));
    }

    @Override
    public List<Project> findAll() {
        return List.copyOf(projectById.values());
    }
}
