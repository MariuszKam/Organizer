package com.organizer.service;

import com.organizer.model.Project;
import com.organizer.model.task.Task;
import com.organizer.model.task.TaskPriority;
import com.organizer.model.task.TaskStatus;
import com.organizer.model.user.User;

import java.util.List;
import java.util.Map;

public class TaskCreationManager {

    private static TaskCreationManager instance;
    private final Map<Project, List<Task>> taskByProject;

    private TaskCreationManager(Map<Project, List<Task>> fetchedProjects) {
        this.taskByProject = fetchedProjects;
    }

    public static TaskCreationManager create(Map<Project, List<Task>> fetchedProjects) {
        if (instance == null) {
            instance = new TaskCreationManager(fetchedProjects);
        }
        return instance;
    }

    public Task createTask(String name, String description, TaskPriority priority, TaskStatus status, User assignedUser) {
        Long taskId = generateTaskId();
        return new Task(taskId, name, description, priority, status, assignedUser);
    }

    private Long generateTaskId() {
        Long maxId = 1L;
        for (List<Task> value : taskByProject.values()) {
            for (Task task : value) {
                if (task.getId() > maxId) {
                    maxId = task.getId();
                }
            }
        }

        return maxId + 1;
    }
}
