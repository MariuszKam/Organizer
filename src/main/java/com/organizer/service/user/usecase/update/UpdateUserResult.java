package com.organizer.service.user.usecase.update;

import com.organizer.model.user.UserId;

public sealed interface UpdateUserResult {
    record Ok(UserId userId) implements UpdateUserResult {
    }

    enum Error implements UpdateUserResult {
        MISSING_COMMAND,
        MISSING_USER_ID,
        INVALID_USER_ID_FORMAT,
        USER_NOT_FOUND,
        USERNAME_ALREADY_EXISTS,
        INVALID_USERNAME_FORMAT,
        MISSING_USERNAME,
        EMAIL_ALREADY_EXISTS,
        MISSING_EMAIL,
        INVALID_EMAIL_FORMAT
    }
}
