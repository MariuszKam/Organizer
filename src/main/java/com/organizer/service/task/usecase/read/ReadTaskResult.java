package com.organizer.service.task.usecase.read;

import com.organizer.model.task.Task;

public sealed interface ReadTaskResult {
    record Ok(Task task) implements ReadTaskResult {
    }

    enum Error implements ReadTaskResult {
        MISSING_TASK_ID, INVALID_TASK_ID_FORMAT, NON_EXISTING_TASK, MISSING_COMMAND

    }
}
