package com.organizer.service.user.adapter;

import com.organizer.model.user.Email;
import com.organizer.model.user.User;
import com.organizer.model.user.UserId;
import com.organizer.model.user.Username;
import com.organizer.service.user.port.UserStore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class InMemoryUserStore implements UserStore {

    private final Map<Username, User> usersByUsername;
    private final Map<Email, User> usersByEmail;
    private final Map<UserId, User> usersById;

    public InMemoryUserStore() {
        this.usersByUsername = new HashMap<>();
        this.usersByEmail = new HashMap<>();
        this.usersById = new HashMap<>();
    }

    @Override
    public boolean existsByEmail(Email email) {
        return usersByEmail.containsKey(email);
    }

    @Override
    public boolean existsByUsername(Username username) {
        return usersByUsername.containsKey(username);
    }

    @Override
    public void save(User user) {
        usersByUsername.put(user.getUsername(), user);
        usersByEmail.put(user.getEmail(), user);
    }

    @Override
    public Optional<User> findById(UserId userId) {
        return Optional.ofNullable(usersById.get(userId));
    }

    @Override
    public List<User> findAll() {
        return List.copyOf(usersByUsername.values());
    }
}
