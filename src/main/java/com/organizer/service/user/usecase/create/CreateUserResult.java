package com.organizer.service.user.usecase.create;

public sealed interface CreateUserResult {
    record Ok(String userId) implements CreateUserResult {}
    enum Error implements CreateUserResult {
        MISSING_COMMAND,
        USERNAME_ALREADY_EXISTS,
        INVALID_USERNAME_FORMAT,
        MISSING_USERNAME,
        EMAIL_ALREADY_EXISTS,
        MISSING_EMAIL,
        INVALID_EMAIL_FORMAT
    }
}
