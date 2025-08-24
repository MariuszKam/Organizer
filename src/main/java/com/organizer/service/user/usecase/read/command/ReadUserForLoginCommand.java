package com.organizer.service.user.usecase.read.command;

public record ReadUserForLoginCommand(String username, String email) implements ReadUserCommand {
}
