package com.organizer.service.user.usecase.update;

import com.organizer.model.user.Email;
import com.organizer.model.user.User;
import com.organizer.model.user.UserId;
import com.organizer.model.user.Username;
import com.organizer.service.user.port.UserStore;

public class UpdateUserService implements UpdateUserUseCase {

    private final UserStore userStore;

    public UpdateUserService(UserStore userStore) {
        this.userStore = userStore;
    }

    @Override
    public UpdateUserResult handle(UpdateUserCommand command) {
        if (command == null) {
            return UpdateUserResult.Error.MISSING_COMMAND;
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

        if (userStore.findById(userId).isEmpty()) {
            return UpdateUserResult.Error.USER_NOT_FOUND;
        }

        Username username;

        if (command.username() == null) {
            return UpdateUserResult.Error.MISSING_USERNAME;
        }

        try {
            username = Username.of(command.username());
        } catch (IllegalArgumentException e) {
            return UpdateUserResult.Error.INVALID_USERNAME_FORMAT;
        }

        if (userStore.existsByUsername(username)) {
            return UpdateUserResult.Error.USERNAME_ALREADY_EXISTS;
        }

        Email email;

        if (command.email() == null) {
            return UpdateUserResult.Error.MISSING_EMAIL;
        }

        try {
            email = Email.of(command.email());
        } catch (IllegalArgumentException e) {
            return UpdateUserResult.Error.INVALID_EMAIL_FORMAT;
        }

        if (userStore.existsByEmail(email)) {
            return UpdateUserResult.Error.EMAIL_ALREADY_EXISTS;
        }

        User user = new User(userId, username, email);
        userStore.save(user);
        return new UpdateUserResult.Ok(userId);
    }
}
