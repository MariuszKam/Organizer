package com.organizer.service.user.usecase.create;

import com.organizer.model.user.Email;
import com.organizer.model.user.User;
import com.organizer.model.user.UserId;
import com.organizer.model.user.Username;
import com.organizer.service.user.port.IdGenerator;
import com.organizer.service.user.port.UserStore;

import java.util.Objects;

public class CreateUserService implements CreateUserUseCase {

    private final UserStore userStore;
    private final IdGenerator idGenerator;

    public CreateUserService(UserStore userStore, IdGenerator idGenerator) {
        this.userStore = Objects.requireNonNull(userStore, "userStore cannot be null");
        this.idGenerator = Objects.requireNonNull(idGenerator, "idGenerator cannot be null");
    }

    @Override
    public CreateUserResult handle(CreateUserCommand command) {
        if (command == null) {
            return CreateUserResult.Error.MISSING_COMMAND;
        }

        Username username;
        if (command.username() == null) {
            return CreateUserResult.Error.MISSING_USERNAME;
        }
        try {
            username = Username.of(command.username());
        } catch (IllegalArgumentException e) {
            return CreateUserResult.Error.INVALID_USERNAME_FORMAT;
        }

        Email email;
        if (command.email() == null) {
            return CreateUserResult.Error.MISSING_EMAIL;
        }
        try {
            email = Email.of(command.email());
        } catch (IllegalArgumentException e) {
            return CreateUserResult.Error.INVALID_EMAIL_FORMAT;
        }

        if (userStore.existsByUsername(username)) {
            return CreateUserResult.Error.USERNAME_ALREADY_EXISTS;
        }

        if (userStore.existsByEmail(email)) {
            return CreateUserResult.Error.EMAIL_ALREADY_EXISTS;
        }

        UserId userId = idGenerator.generateId();

        User user = new User(userId, username, email);
        userStore.save(user);
        return new CreateUserResult.Ok(userId.toString());
    }
}
