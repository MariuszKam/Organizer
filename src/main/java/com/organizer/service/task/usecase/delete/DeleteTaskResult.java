package com.organizer.service.task.usecase.delete;

import com.organizer.model.task.TaskId;

public sealed interface DeleteTaskResult {
    record Ok(TaskId taskId) implements DeleteTaskResult {
    }

    enum Error implements DeleteTaskResult {
        MISSING_TASK_ID, INVALID_TASK_ID_FORMAT, NON_EXISTING_TASK, MISSING_COMMAND

    }
}
