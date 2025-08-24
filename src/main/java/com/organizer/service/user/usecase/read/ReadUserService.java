package com.organizer.service.user.usecase.read;

import com.organizer.model.user.Email;
import com.organizer.model.user.User;
import com.organizer.model.user.UserId;
import com.organizer.model.user.Username;
import com.organizer.service.user.port.UserStore;
import com.organizer.service.user.usecase.read.command.ReadUserByIdCommand;
import com.organizer.service.user.usecase.read.command.ReadUserCommand;
import com.organizer.service.user.usecase.read.command.ReadUserForLoginCommand;

import java.util.Objects;

public class ReadUserService implements ReadUserUseCase {

    private final UserStore userStore;

    public ReadUserService(UserStore userStore) {
        Objects.requireNonNull(userStore, "User Store cannot be null");
        this.userStore = userStore;
    }

    @Override
    public ReadUserResult handle(ReadUserCommand command) {
        if (command == null) {
            return ReadUserResult.Error.MISSING_COMMAND;
        }

        return switch (command) {
            case ReadUserByIdCommand byId -> handleReadById(byId.id());
            case ReadUserForLoginCommand login -> handleReadLogin(login.username(), login.email());
        };
    }

    private ReadUserResult handleReadById(String id) {
        if (id == null) {
            return ReadUserResult.Error.MISSING_USER_ID;
        }

        UserId userId;
        try {
            userId = UserId.of(id);
        } catch (IllegalArgumentException e) {
            return ReadUserResult.Error.INVALID_USER_ID_FORMAT;
        }

        User user = userStore.findById(userId).orElse(null);
        if (user == null) {
            return ReadUserResult.Error.USER_NOT_FOUND;
        }
        return new ReadUserResult.Ok(user);
    }

    private ReadUserResult handleReadLogin(String username, String email) {
        if (username == null && email == null) {
            return ReadUserResult.Error.NO_PROVIDED_PARAMETERS;
        }

        if (username == null) {
            return ReadUserResult.Error.MISSING_USERNAME;
        } else if (email == null) {
            return ReadUserResult.Error.MISSING_EMAIL;
        }

        Username usernameObj;
        try {
            usernameObj = Username.of(username);
        } catch (IllegalArgumentException e) {
            return ReadUserResult.Error.INVALID_USERNAME_FORMAT;
        }

        Email emailObj;
        try {
            emailObj = Email.of(email);
        } catch (IllegalArgumentException e) {
            return ReadUserResult.Error.INVALID_EMAIL_FORMAT;
        }

        User userByUsername = userStore.findByUsername(usernameObj).orElse(null);
        if (userByUsername == null) {
            return ReadUserResult.Error.USERNAME_NOT_FOUND;
        }

        User userByEmail = userStore.findByEmail(emailObj).orElse(null);
        if (userByEmail == null) {
            return ReadUserResult.Error.EMAIL_NOT_FOUND;
        }

        if (userByUsername.equals(userByEmail)) {
            return new ReadUserResult.Ok(userByUsername);
        } else {
            return ReadUserResult.Error.MISMATCH;
        }
    }
}
