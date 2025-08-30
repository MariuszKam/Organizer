package com.organizer.service.task.usecase.update;

import com.organizer.model.task.TaskId;

public sealed interface UpdateTaskResult {
    record Ok(TaskId taskId) implements UpdateTaskResult {
    }

    enum Error implements UpdateTaskResult {
        MISSING_TASK_ID,
        NO_FIELDS_PROVIDED,
        INVALID_TASK_ID_FORMAT,
        NON_EXISTING_TASK,
        INVALID_TASK_NAME_FORMAT,
        INVALID_TASK_DESCRIPTION_FORMAT,
        INVALID_TASK_PRIORITY_FORMAT,
        INVALID_TASK_STATUS_FORMAT,
        INVALID_USERNAME_FORMAT,
        MISSING_COMMAND

    }
}
