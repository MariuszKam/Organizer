package com.organizer.service.user.usecase.read;

import com.organizer.service.user.usecase.read.command.ReadUserCommand;

public interface ReadUserUseCase {
    ReadUserResult handle(ReadUserCommand command);
}
