package com.organizer.service.task;

import com.organizer.model.Project;

import java.util.ArrayList;
import java.util.List;

public final class TaskManager {

    private static TaskManager instance;
    private final List<Project> projectList;
    private final TaskCreationManager taskCreationManager;

    private TaskManager() {
        this.projectList = new ArrayList<>();
        this.taskCreationManager = TaskCreationManager.create(projectList);
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
