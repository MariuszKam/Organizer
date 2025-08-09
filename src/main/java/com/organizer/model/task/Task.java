package com.organizer.model.task;

import com.organizer.model.user.User;

import java.util.Objects;

public final class Task {

    private final Long id;
    private TaskName name;
    private TaskDescription description;
    private TaskPriority priority;
    private TaskStatus status;
    private User assignedUser;

    public Task(Long id, TaskName name, TaskDescription description) {
        Objects.requireNonNull(id, "Task ID cannot be null");
        this.id = id;
        Objects.requireNonNull(name, "Task name cannot be null");
        this.name = name;
        Objects.requireNonNull(description, "Task description cannot be null");
        this.description = description;
        this.priority = TaskPriority.MEDIUM;
        this.status = TaskStatus.TODO;
        this.assignedUser = null;
    }

    public Task(Long id, TaskName name, TaskDescription description, TaskPriority priority, TaskStatus status, User assignedUser) {
        this(id, name, description);
        Objects.requireNonNull(priority, "Task priority cannot be null");
        this.priority = priority;
        Objects.requireNonNull(status, "Task status cannot be null");
        this.status = status;
        Objects.requireNonNull(assignedUser, "Assigned user cannot be null");
        this.assignedUser = assignedUser;
    }

    public Long getId() {
        return id;
    }

    public TaskName getName() {
        return name;
    }

    public TaskDescription getDescription() {
        return description;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public User getAssignedUser() {
        return assignedUser;
    }

    public void changeTaskName(TaskName name) {
        Objects.requireNonNull(name, "Task name cannot be null");
        this.name = name;
    }

    public void changeTaskDescription(TaskDescription description) {
        Objects.requireNonNull(description, "Task description cannot be null");
        this.description = description;
    }

    public void changePriority(TaskPriority priority) {
        Objects.requireNonNull(priority, "Task priority cannot be null");
        this.priority = priority;
    }

    public void changeStatus(TaskStatus status) {
        Objects.requireNonNull(status, "Task status cannot be null");
        this.status = status;
    }

    public void assignUser(User user) {
        Objects.requireNonNull(user, "Assigned user cannot be null");
        this.assignedUser = user;
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
