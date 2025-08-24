package com.organizer.service.user.usecase.read;

import com.organizer.model.user.User;

public sealed interface ReadUserResult {
    record Ok(User user) implements ReadUserResult {
    }

    enum Error implements ReadUserResult {
        MISSING_COMMAND,
        MISSING_USER_ID,
        INVALID_USER_ID,
        USER_NOT_FOUND,
        NO_PROVIDED_PARAMETERS,
        MISSING_USERNAME,
        INVALID_USERNAME_FORMAT,
        USERNAME_NOT_FOUND,
        MISSING_EMAIL,
        INVALID_EMAIL_FORMAT,
        EMAIL_NOT_FOUND,
        MISMATCH
    }
}
