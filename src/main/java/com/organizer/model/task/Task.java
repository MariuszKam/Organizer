package com.organizer.model.task;

public class Task {

    private final Long id;
    private String name;
    private String description;
    private TaskPriority priority;
    private TaskStatus status;

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
}
