package com.organizer.service.user.usecase.update;

import com.organizer.model.user.Email;
import com.organizer.model.user.User;
import com.organizer.model.user.UserId;
import com.organizer.model.user.Username;
import com.organizer.service.user.port.UserStore;

import java.util.Objects;

public class UpdateUserService implements UpdateUserUseCase {

    private final UserStore userStore;

    public UpdateUserService(UserStore userStore) {
        Objects.requireNonNull(userStore, "User Store cannot be null");
        this.userStore = userStore;
    }

    @Override
    public UpdateUserResult handle(UpdateUserCommand command) {
        if (command == null) {
            return UpdateUserResult.Error.MISSING_COMMAND;
        }

        if (command.username().isEmpty() && command.email().isEmpty()) {
            return UpdateUserResult.Error.NO_FIELDS_PROVIDED;
        }

        if (command.userId() == null) {
            return UpdateUserResult.Error.MISSING_USER_ID;
        }

        UserId userId;
        try {
            userId = UserId.of(command.userId());
        } catch (IllegalArgumentException e) {
            return UpdateUserResult.Error.INVALID_USER_ID_FORMAT;
        }

        User existingUser = userStore.findById(userId).orElse(null);

        if (existingUser == null) {
            return UpdateUserResult.Error.USER_NOT_FOUND;
        }

        Username username;
        try {
            username = command.username()
                    .map(Username::of)
                    .orElse(existingUser.getUsername());
        } catch (IllegalArgumentException e) {
            return UpdateUserResult.Error.INVALID_USERNAME_FORMAT;
        }

        Email email;
        try {
            email = command.email()
                    .map(Email::of)
                    .orElse(existingUser.getEmail());
        } catch (IllegalArgumentException e) {
            return UpdateUserResult.Error.INVALID_EMAIL_FORMAT;
        }

        if (username.equals(existingUser.getUsername()) &&
                email.equals(existingUser.getEmail())) {
            return UpdateUserResult.Error.NO_CHANGES;
        }

        if (userStore.existsByUsername(username)
                && !existingUser.getUsername().equals(username)) {
            return UpdateUserResult.Error.USERNAME_ALREADY_EXISTS;
        }
        if (userStore.existsByEmail(email)
                && !existingUser.getEmail().equals(email)) {
            return UpdateUserResult.Error.EMAIL_ALREADY_EXISTS;
        }

        User user = new User(userId, username, email);
        userStore.save(user);
        return new UpdateUserResult.Ok(userId);
    }
}
