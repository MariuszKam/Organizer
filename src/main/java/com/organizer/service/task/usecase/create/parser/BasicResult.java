package com.organizer.service.task.usecase.create.parser;

import com.organizer.model.task.TaskDescription;
import com.organizer.model.task.TaskName;

public record BasicResult(TaskName name, TaskDescription taskDescription) {
}
