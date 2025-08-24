package com.organizer.service.user.port;

import com.organizer.model.user.Email;
import com.organizer.model.user.User;
import com.organizer.model.user.UserId;
import com.organizer.model.user.Username;

import java.util.List;
import java.util.Optional;

public interface UserStore {
    boolean existsByEmail(Email email);

    boolean existsByUsername(Username username);

    void save(User user);

    Optional<User> findById(UserId userId);

    Optional<User> findByUsername(Username username);

    Optional<User> findByEmail(Email email);

    List<User> findAll();
}
