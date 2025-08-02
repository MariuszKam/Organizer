package com.organizer.service;

import com.organizer.model.Project;
import com.organizer.model.task.Task;

import java.util.ArrayList;
import java.util.List;

public final class TaskManager {

    private static TaskManager instance;
    private final List<Project> projectList;

    private TaskManager() {
        this.projectList = new ArrayList<>();
    }

    public static TaskManager create() {
        if (instance == null) {
            instance = new TaskManager();
        }
        return instance;
    }

    public static void createNewTask(Task task, Project project) {
        if (instance == null) {
            throw new IllegalStateException("TaskManager is not initialized. Call create() first.");
        }
        if (task == null || project == null) {
            throw new IllegalArgumentException("Task and Project cannot be null.");
        }
        project.addTask(task);
    }
}
