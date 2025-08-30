package com.organizer.service.task.usecase.create;

import com.organizer.model.task.TaskId;

public sealed interface CreateTaskResult {
    record Ok(TaskId taskId) implements CreateTaskResult {
    }

    enum Error implements CreateTaskResult {
        MISSING_COMMAND,
        MISSING_TASK_NAME,
        MISSING_TASK_DESCRIPTION,
        INVALID_TASK_NAME_FORMAT,
        INVALID_TASK_DESCRIPTION_FORMAT,
        MISSING_TASK_PRIORITY,
        INVALID_TASK_PRIORITY_NAME,
        MISSING_TASK_STATUS,
        INVALID_TASK_STATUS_NAME,
        MISSING_USERNAME,
        INVALID_USERNAME, NON_EXISTING_USER,
    }

}
