package com.organizer.model.task;

import java.util.Objects;
import java.util.regex.Pattern;

public record TaskName(String name) {

    private static final Pattern PATTERN = Pattern.compile("^[a-zA-Z0-9_\\- ]{1,50}$");

    public TaskName {
        Objects.requireNonNull(name(), "Task name cannot be null");
        var taskName = name().strip();
        if (!PATTERN.matcher(name).matches()) throw new IllegalArgumentException(String.format("Invalid task name: %s", taskName));
        name = taskName;
    }

    public static TaskName of(String name) {
        return new TaskName(name);
    }
}
