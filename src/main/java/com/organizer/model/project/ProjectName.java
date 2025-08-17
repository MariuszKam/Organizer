package com.organizer.model.project;

import java.util.Objects;
import java.util.regex.Pattern;

public record ProjectName(String name) {

    private static final Pattern PATTERN = Pattern.compile("^.{1,50}$");

    public ProjectName {
        Objects.requireNonNull(name, "Project name cannot be null");
        var projectName = name.strip();
        if (!PATTERN.matcher(projectName).matches()) {
            throw new IllegalArgumentException(String.format("Invalid project name: %s", projectName));
        }
        name = projectName;
    }

    public static ProjectName of(String name) {
        return new ProjectName(name);
    }

}
