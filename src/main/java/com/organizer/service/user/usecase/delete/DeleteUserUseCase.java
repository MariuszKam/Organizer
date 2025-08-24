package com.organizer.service.user.usecase.delete;

public interface DeleteUserUseCase {
    DeleteUserResult handle(DeleteUserCommand command);
}
