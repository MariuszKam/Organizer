package com.organizer.service.task;

import com.organizer.model.Project;
import com.organizer.model.task.Task;
import com.organizer.model.task.TaskPriority;
import com.organizer.model.task.TaskStatus;
import com.organizer.model.user.User;

import java.util.List;

public final class TaskCreationManager {

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

    public Project addTaskToProject(Task task, Project project) {
        if (project == null) {
            throw new IllegalArgumentException("Project and Task cannot be null");
        }
        project.addTask(task);
        return project;
    }

    private Long generateTaskId() {
        Long maxId = 0L;
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
