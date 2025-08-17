package com.organizer.model.task;

import java.util.Objects;
import java.util.UUID;

public record TaskId(UUID value) {

    private static final UUID NIL = new UUID(0L, 0L);

    public TaskId {
        Objects.requireNonNull(value, "Task ID cannot be null");
        if (value.equals(NIL)) {
            throw new IllegalArgumentException("Task ID cannot be NIL UUID");
        }
    }

    public static TaskId newId() {
        return new TaskId(UUID.randomUUID());
    }

    public static TaskId of(String string) {
        Objects.requireNonNull(string, "Task ID string cannot be null");
        UUID uuid = UUID.fromString(string.strip());
        return new TaskId(uuid);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
