package com.organizer.service.user.usecase.update;

import com.organizer.model.user.Email;
import com.organizer.model.user.User;
import com.organizer.model.user.UserId;
import com.organizer.model.user.Username;
import com.organizer.service.user.port.UserStore;

import java.util.Optional;

public class UpdateUserService implements UpdateUserUseCase {

    private final UserStore userStore;

    public UpdateUserService(UserStore userStore) {
        this.userStore = userStore;
    }

    @Override
    public UpdateUserResult handle(UpdateUserCommand command) {
        if (command == null) {
            return UpdateUserResult.Error.MISSING_COMMAND;
        } else if (command.username().isEmpty() && command.email().isEmpty()) {
            return UpdateUserResult.Error.NO_FIELDS_PROVIDED;
        }

        UserId userId;

        if (command.userId() == null) {
            return UpdateUserResult.Error.MISSING_USER_ID;
        }

        try {
            userId = UserId.of(command.userId());
        } catch (IllegalArgumentException e) {
            return UpdateUserResult.Error.INVALID_USER_ID_FORMAT;
        }

        Optional<User> existingUser = userStore.findById(userId);

        if (existingUser.isEmpty()) {
            return UpdateUserResult.Error.USER_NOT_FOUND;
        }

        Username username;

        if (command.username().isPresent()) {
            try {
                username = Username.of(command.username().get());
            } catch (IllegalArgumentException e) {
                return UpdateUserResult.Error.INVALID_USERNAME_FORMAT;
            }
            if (userStore.existsByUsername(username)) {
                return UpdateUserResult.Error.USERNAME_ALREADY_EXISTS;
            }
        } else {
            username = existingUser.get().getUsername();
        }

        Email email;

        if (command.email().isPresent()) {
            try {
                email = Email.of(command.email().get());
            } catch (IllegalArgumentException e) {
                return UpdateUserResult.Error.INVALID_EMAIL_FORMAT;
            }
            if (userStore.existsByEmail(email)) {
                return UpdateUserResult.Error.EMAIL_ALREADY_EXISTS;
            }
        } else {
            email = existingUser.get().getEmail();
        }

        User user = new User(userId, username, email);
        userStore.save(user);
        return new UpdateUserResult.Ok(userId);
    }
}
