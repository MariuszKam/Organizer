package com.organizer.model.task;

import com.organizer.model.user.User;

public class Task {

    private final Long id;
    private String name;
    private String description;
    private TaskPriority priority;
    private TaskStatus status;
    private User assignedUser;

    public Task(Long id, String name, String description) {
        if (id == null) {
            throw new IllegalArgumentException("Task ID cannot be null");
        }
        this.id = id;
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Task name cannot be null or empty");
        }
        this.name = name;
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Task description cannot be null or empty");
        }
        this.description = description;
        this.priority = TaskPriority.MEDIUM;
        this.status = TaskStatus.TODO;
        this.assignedUser = null;
    }

    public Task(Long id, String name, String description, TaskPriority priority, TaskStatus status, User assignedUser) {
        this(id, name, description);
        this.priority = priority;
        this.status = status;
        this.assignedUser = assignedUser;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public User getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(User assignedUser) {
        this.assignedUser = assignedUser;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;
        return id.equals(task.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                ", status=" + status +
                ", assignedUser=" + assignedUser +
                '}';
    }
}
