package com.organizer.model.user;

import java.util.Objects;
import java.util.UUID;

public record UserId(UUID value) {

    private static final java.util.UUID NIL = new UUID(0L, 0L);

    public UserId {
        Objects.requireNonNull(value, "User ID cannot be null");
        if (value.equals(NIL)) {
            throw new IllegalArgumentException("User ID cannot be NIL UUID");
        }
    }

    public static UserId newId() {
        return new UserId(UUID.randomUUID());
    }

    public static UserId of(String string) {
        Objects.requireNonNull(string, "User ID string cannot be null");
        UUID uuid = UUID.fromString(string);
        return new UserId(uuid);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
