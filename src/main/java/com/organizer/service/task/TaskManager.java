package com.organizer.service.task;

import com.organizer.model.Project;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TaskManager {

    private static TaskManager instance;
    private final Map<Project, List<Task>> taskByProject;
    private final TaskCreationManager taskCreationManager;

    private TaskManager() {
        this.taskByProject = new HashMap<>();
        this.taskCreationManager = TaskCreationManager.create(taskByProject);
    }

    public static TaskManager create() {
        if (instance == null) {
            instance = new TaskManager();
        }
        return instance;
    }

    public TaskCreationManager getTaskCreationManager() {
        return taskCreationManager;
    }
}
