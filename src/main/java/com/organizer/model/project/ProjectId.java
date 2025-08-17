package com.organizer.model.project;

import java.util.Objects;
import java.util.UUID;

public record ProjectId(UUID value) {

    public ProjectId {
        Objects.requireNonNull(value, "Project ID cannot be null");
        if (value.equals(new java.util.UUID(0L, 0L))) {
            throw new IllegalArgumentException("Project ID cannot be NIL UUID");
        }
    }

    public static ProjectId newId() {
        return new ProjectId(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
