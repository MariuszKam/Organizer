package com.organizer.model;

public class Task {

    private final Long id;
    private String name;
    private String description;
    private TaskPriority priority;
    private TaskStatus status;

    public Task(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.priority = TaskPriority.MEDIUM;
        this.status = TaskStatus.TODO;
    }
}
