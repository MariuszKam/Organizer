package com.organizer.service.task.adapter;

import com.organizer.model.task.Task;
import com.organizer.model.task.TaskId;
import com.organizer.service.task.port.TaskStore;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class InMemoryTaskStore implements TaskStore {

    private final HashMap<TaskId, Task> taskById;

    public InMemoryTaskStore() {
        this.taskById = new HashMap<>();
    }

    @Override
    public void save(Task task) {
        Objects.requireNonNull(task, "Task cannot be null");
        taskById.put(task.getId(), task);
    }

    @Override
    public void remove(Task task) {
        Objects.requireNonNull(task, "Task cannot be null");
        taskById.remove(task.getId());
    }

    @Override
    public Optional<Task> findById(TaskId taskId) {
        return Optional.ofNullable(taskById.get(taskId));
    }

    @Override
    public List<Task> findAll() {
        return List.copyOf(taskById.values());
    }
}
