package com.organizer.service.task.usecase.update;

import java.util.Optional;

public record UpdateTaskCommand(String taskId,
                                Optional<String> name,
                                Optional<String> description,
                                Optional<String> priority,
                                Optional<String> status,
                                Optional<String> username) {
}
