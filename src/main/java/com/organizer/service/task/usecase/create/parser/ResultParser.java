package com.organizer.service.task.usecase.create.parser;

import com.organizer.service.task.usecase.create.CreateTaskResult;

public sealed interface ResultParser {
    record Ok<T>(T value) implements ResultParser {
    }

    record Err(CreateTaskResult.Error error) implements ResultParser {
    }
}
