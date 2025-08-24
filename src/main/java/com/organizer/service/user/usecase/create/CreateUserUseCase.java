package com.organizer.service.user.usecase.create;

public interface CreateUserUseCase {

    CreateUserResult handle(CreateUserCommand command);
}
