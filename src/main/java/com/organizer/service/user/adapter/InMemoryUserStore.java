package com.organizer.service.user.adapter;

import com.organizer.model.user.Email;
import com.organizer.model.user.User;
import com.organizer.model.user.UserId;
import com.organizer.model.user.Username;
import com.organizer.service.user.port.UserStore;

import java.util.*;

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

    /**
     * Saves or updates a user in the store.
     * If a user with the same ID already exists, it will be updated.
     * If the username or email has changed, the old entries will be removed.
     *
     * @param user the user to save or update
     */

    @Override
    public void save(User user) {
        Objects.requireNonNull(user, "user cannot be null");

        User owner = usersByUsername.get(user.getUsername());
        if (owner != null && !owner.getId().equals(user.getId())) {
            throw new IllegalArgumentException("Username already exists: " + user.getUsername());
        }

        owner = usersByEmail.get(user.getEmail());
        if (owner != null && !owner.getId().equals(user.getId())) {
            throw new IllegalArgumentException("Email already exists: " + user.getEmail());
        }

        if (usersById.containsKey(user.getId())) {
            User existingUser = usersById.get(user.getId());
            if (existingUser.getUsername().equals(user.getUsername()) && existingUser.getEmail().equals(user.getEmail())) {
                // No changes in username or email, just return
                return;
            } else {
                usersByUsername.remove(existingUser.getUsername());
                usersByEmail.remove(existingUser.getEmail());
            }
        }
        usersByUsername.put(user.getUsername(), user);
        usersByEmail.put(user.getEmail(), user);
        usersById.put(user.getId(), user);
    }

    @Override
    public Optional<User> findById(UserId userId) {
        return Optional.ofNullable(usersById.get(userId));
    }

    @Override
    public List<User> findAll() {
        return List.copyOf(usersById.values());
    }

}
