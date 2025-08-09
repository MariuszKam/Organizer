package com.organizer.model.user;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

public record Username(String username) {

    private static final Pattern PATTERN = Pattern.compile("^[a-zA-Z]{4,10}$");

    public Username {
        Objects.requireNonNull(username, "Username cannot be null");
        var name = username.strip().toLowerCase(Locale.ROOT);
        if (!PATTERN.matcher(name).matches()) throw new IllegalArgumentException(String.format("Invalid username: %s", name));
        username = name;
    }

    public static Username of(String username) {
        return new Username(username);
    }
}
