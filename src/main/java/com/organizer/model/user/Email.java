package com.organizer.model.user;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

public record Email(String emailAddress) {

    private static final Pattern PATTERN = Pattern.compile("^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public Email {
        Objects.requireNonNull(emailAddress, "Email cannot be null");
        var email = emailAddress.strip().toLowerCase(Locale.ROOT);
        if (!PATTERN.matcher(email).matches()) throw new IllegalArgumentException(String.format("Invalid email: %s", email));
        emailAddress = email;
    }

    public static Email of(String emailAddress) {
        return new Email(emailAddress);
    }

    @Override
    public String toString() {
        int at = emailAddress.indexOf('@');
        return emailAddress.charAt(0) + "***" + emailAddress.substring(at);
    }
}
