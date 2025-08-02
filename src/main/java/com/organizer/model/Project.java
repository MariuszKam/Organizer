package com.organizer.model;

import com.organizer.model.task.Task;

import java.util.ArrayList;
import java.util.List;

public class Project {

    private final Long id;
    private String name;
    private final List<Task> taskList;

    public Project(Long id, String name) {
        if (id == null) {
            throw new IllegalArgumentException("Project ID cannot be null");
        }
        this.id = id;
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Project name cannot be null or empty");
        }
        this.name = name;
        this.taskList = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    @Override
    public boolean equals(Object o) {
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
                ", taskList=" + taskList +
                '}';
    }
}
