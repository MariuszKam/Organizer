package com.organizer.service.user;

import com.organizer.model.user.Email;
import com.organizer.model.user.User;

import java.util.HashMap;
import java.util.Map;

public final class UserService {

    private static UserService instance;
    private final Map<String, User> users;

    private UserService() {
        this.users = new HashMap<>();
    }

    public static UserService create() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public User createUser(Long id, String username, String emailAddress) {
        if (id == null || username == null || emailAddress == null) {
            throw new IllegalArgumentException("User ID, username, and email cannot be null");
        }
        Email email = new Email(emailAddress);
        User user = new User(id, username, email);
        users.put(username, user);
        return user;
    }

    public boolean isUserExists(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        return users.containsKey(username);
    }

}
