package com.organizer.model.task;

import java.util.Objects;
import java.util.regex.Pattern;

public record TaskDescription(String description) {

    private static final Pattern PATTERN = Pattern.compile("^.{1,500}$", Pattern.DOTALL);

    public TaskDescription {
        Objects.requireNonNull(description, "Task description cannot be null");
        if (!PATTERN.matcher(description).matches()) throw new IllegalArgumentException("Invalid task description! Description must be between 1 and 500 characters.");
    }

    public static TaskDescription of(String description) {
        return new TaskDescription(description);
    }
}
