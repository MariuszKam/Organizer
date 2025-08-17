package com.organizer.model.user;

import java.util.Objects;

public final class User {

    private final UserId id;
    private Username username;
    private Email email;

    public User(Username username, Email email) {
        this(UserId.newId(), username, email);
    }

    public User(UserId id, Username username, Email email) {
        Objects.requireNonNull(id, "User ID cannot be null");
        this.id = id;
        Objects.requireNonNull(username, "Username cannot be null");
        this.username = username;
        Objects.requireNonNull(email, "Email cannot be null");
        this.email = email;
    }

    public UserId getId() {
        return id;
    }

    public Username getUsername() {
        return username;
    }

    public Email getEmail() {
        return email;
    }

    public void changeUsername(Username newUsername) {
        Objects.requireNonNull(newUsername, "Username cannot be null");
        if (username.equals(newUsername)) throw new IllegalArgumentException("New username is the same");
        username = newUsername;
    }

    public void changeEmail(Email newEmail) {
        Objects.requireNonNull(newEmail, "Email cannot be null");
        if (email.equals(newEmail)) throw new IllegalArgumentException("New email is the same");
        email = newEmail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
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
