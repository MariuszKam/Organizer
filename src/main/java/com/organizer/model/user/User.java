package com.organizer.model.user;

import java.util.Locale;

public final class User {

    private final Long id;
    private String username;
    private Email email;

    public User(Long id, String username, Email email) {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        this.id = id;
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        this.username = username.strip().toLowerCase(Locale.ROOT);
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Email getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email=" + email +
                '}';
    }
}
