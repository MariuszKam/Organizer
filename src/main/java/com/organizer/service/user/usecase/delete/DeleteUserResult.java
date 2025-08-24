package com.organizer.service.user.usecase.delete;

import com.organizer.model.user.UserId;

public sealed interface DeleteUserResult {
    record Ok(UserId userId) implements DeleteUserResult {
    }

    enum Error implements DeleteUserResult {
        MISSING_COMMAND,
        MISSING_USER_ID,
        INVALID_USER_ID_FORMAT,
        USER_NOT_FOUND
    }
}
