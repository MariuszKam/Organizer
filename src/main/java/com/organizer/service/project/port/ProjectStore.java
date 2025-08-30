package com.organizer.service.project.port;

import com.organizer.model.project.Project;
import com.organizer.model.project.ProjectId;

import java.util.List;
import java.util.Optional;

public interface ProjectStore {
    void save(Project project);

    void remove(Project project);

    Optional<Project> findById(ProjectId projectId);

    List<Project> findAll();
}
