package com.organizer.service.task.usecase.read;

import com.organizer.model.task.Task;

public sealed interface ReadTaskResult {
    record Ok(Task task) implements ReadTaskResult {
    }

    enum Error implements ReadTaskResult {

    }
}
