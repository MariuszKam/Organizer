package com.organizer.model.task;

import java.util.Objects;
import java.util.UUID;

public record TaskId(UUID value) {

    public TaskId {
        Objects.requireNonNull(value, "Task ID cannot be null");
        if (value.equals(new UUID(0L, 0L))) {
            throw new IllegalArgumentException("Task ID cannot be NIL UUID");
        }
    }

    public static TaskId newId() {
        return new TaskId(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
