package com.organizer.service.user.usecase.update;

import com.organizer.model.user.UserId;

public sealed interface UpdateUserResult {
    record Ok(UserId userId) implements UpdateUserResult {
    }

    enum Error implements UpdateUserResult {
        MISSING_COMMAND,
        NO_FIELDS_PROVIDED,
        MISSING_USER_ID,
        INVALID_USER_ID_FORMAT,
        USER_NOT_FOUND,
        NO_CHANGES,
        USERNAME_ALREADY_EXISTS,
        INVALID_USERNAME_FORMAT,
        EMAIL_ALREADY_EXISTS,
        INVALID_EMAIL_FORMAT
    }
}
