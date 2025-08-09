package com.organizer.service.user;

import com.organizer.model.user.Email;
import com.organizer.model.user.User;

import java.util.HashSet;
import java.util.Set;

public final class UserService {

    private static UserService instance;
    private final Set<User> users;

    private UserService() {
        this.users = new HashSet<>();
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
        users.add(user);
        return user;
    }

    public boolean isUserExists(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return users.contains(user);
    }

}
