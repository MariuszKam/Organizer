package com.organizer.service.user.usecase.delete;

import com.organizer.model.user.User;
import com.organizer.model.user.UserId;
import com.organizer.service.user.port.UserStore;

import java.util.Objects;

public class DeleteUserService implements DeleteUserUseCase {

    private final UserStore userStore;

    public DeleteUserService(UserStore userStore) {
        Objects.requireNonNull(userStore, "User Store cannot be null");
        this.userStore = userStore;
    }

    @Override
    public DeleteUserResult handle(DeleteUserCommand command) {
        if (command == null) {
            return DeleteUserResult.Error.MISSING_COMMAND;
        }
        if (command.id() == null) {
            return DeleteUserResult.Error.MISSING_USER_ID;
        }

        UserId userId;
        try {
            userId = UserId.of(command.id());
        } catch (IllegalArgumentException e) {
            return DeleteUserResult.Error.INVALID_USER_ID_FORMAT;
        }

        User user = userStore.findById(userId).orElse(null);
        if (user == null) {
            return DeleteUserResult.Error.USER_NOT_FOUND;
        }

        userStore.remove(user);
        return new DeleteUserResult.Ok(userId);
    }
}
