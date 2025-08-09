package com.organizer.service.task;

import com.organizer.model.Project;

import java.util.ArrayList;
import java.util.List;

public final class TaskManager {

    private static TaskManager instance;
    private final List<Project> taskByProject;
    private final TaskCreationManager taskCreationManager;

    private TaskManager() {
        this.taskByProject = new ArrayList<>();
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
