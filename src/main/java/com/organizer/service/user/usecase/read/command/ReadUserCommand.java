package com.organizer.service.user.usecase.read.command;

public sealed interface ReadUserCommand permits ReadUserByIdCommand, ReadUserForLoginCommand {

}
