package com.organizer.model.user;

import java.util.Objects;
import java.util.UUID;

public record UserId(UUID value) {

    public UserId {
        Objects.requireNonNull(value, "User ID cannot be null");
        if (value.equals(new UUID(0L, 0L))) {
            throw new IllegalArgumentException("User ID cannot be NIL UUID");
        }
    }

    public static UserId newId() {
        return new UserId(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
