package com.organizer.service.task.port;

import com.organizer.model.task.Task;
import com.organizer.model.task.TaskId;

import java.util.List;
import java.util.Optional;

public interface TaskStore {
    void save(Task task);

    void remove(Task task);

    Optional<Task> findById(TaskId taskId);

    List<Task> findAll();
}
