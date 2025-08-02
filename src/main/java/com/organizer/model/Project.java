package com.organizer.model;

import java.util.List;

public class Project {

    private final Long id;
    private String name;
    private final List<Task> taskList;

    public Project(Long id, String name, List<Task> taskList) {
        this.id = id;
        this.name = name;
        this.taskList = taskList;
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

    public List<Task> getTaskList() {
        return taskList;
    }

    public boolean addTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null.");
        }
        return taskList.add(task);
    }
}
