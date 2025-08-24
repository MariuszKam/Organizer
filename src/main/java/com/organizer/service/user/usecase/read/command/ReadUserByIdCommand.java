package com.organizer.service.user.usecase.read.command;

public record ReadUserByIdCommand(String id) implements ReadUserCommand {
}
