package com.organizer.service;

import com.organizer.model.Project;
import com.organizer.model.task.Task;
import com.organizer.model.task.TaskPriority;
import com.organizer.model.task.TaskStatus;
import com.organizer.model.user.User;

import java.util.List;

public class TaskCreationManager {

    private static TaskCreationManager instance;
    private final List<Project> projectList;

    private TaskCreationManager(List<Project> fetchedProjects) {
        this.projectList = fetchedProjects;
    }

    public static TaskCreationManager create(List<Project> fetchedProjects) {
        if (instance == null) {
            instance = new TaskCreationManager(fetchedProjects);
        }
        return instance;
    }

    public Task createTask(String name, String description, TaskPriority priority, TaskStatus status, User assignedUser) {
        Long taskId = generateTaskId();
        return new Task(taskId, name, description, priority, status, assignedUser);
    }

    public boolean addTaskToProject(Long projectId, String taskName, String taskDescription) {
        for (Project project : projectList) {
            if (project.getId().equals(projectId)) {
                return project.addTask(taskName, taskDescription);
            }
        }
        return false; // Project not found
    }

    private Long generateTaskId() {
        Long maxId = 1L;
        for (Project project : projectList) {
            for (Task task : project.getTaskList()) {
                if (task.getId() > maxId) {
                    maxId = task.getId();
                }
            }
        }
        return maxId + 1;
    }
}
