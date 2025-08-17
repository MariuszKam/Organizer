package com.organizer.model.project;

import com.organizer.model.task.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Project {

    private final ProjectId id;
    private ProjectName name;
    private final List<Task> taskList;

    public Project(ProjectName name) {
        this(ProjectId.newId(), name);
    }

    public Project(ProjectId id, ProjectName name) {
        Objects.requireNonNull(id, "Project ID cannot be null");
        this.id = id;
        Objects.requireNonNull(name, "Project name cannot be null");
        this.name = name;
        this.taskList = new ArrayList<>();
    }

    public ProjectId getId() {
        return id;
    }

    public ProjectName getName() {
        return name;
    }

    public void changeProjectName(ProjectName name) {
        Objects.requireNonNull(name, "Project name cannot be null");
        this.name = name;
    }

    public List<Task> getTaskList() {
        return List.copyOf(taskList);
    }

    public boolean addTask(Task task) {
        Objects.requireNonNull(task, "Task cannot be null");
        if (taskList.contains(task)) {
            throw new IllegalArgumentException("Task already exists in the project");
        }
        return taskList.add(task);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Project project = (Project) o;
        return id.equals(project.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", tasks=" + taskList.size() +
                '}';
    }
}
