package com.organizer.model.project;

import java.util.Objects;
import java.util.UUID;

public record ProjectId(UUID value) {

    private static final UUID NIL = new UUID(0L, 0L);

    public ProjectId {
        Objects.requireNonNull(value, "Project ID cannot be null");
        if (value.equals(NIL)) {
            throw new IllegalArgumentException("Project ID cannot be NIL UUID");
        }
    }

    public static ProjectId newId() {
        return new ProjectId(UUID.randomUUID());
    }

    public static ProjectId of(String string) {
        Objects.requireNonNull(string, "Project ID string cannot be null");
        UUID uuid = UUID.fromString(string.strip());
        return new ProjectId(uuid);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
