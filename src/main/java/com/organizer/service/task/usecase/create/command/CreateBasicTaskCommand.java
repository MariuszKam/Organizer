package com.organizer.service.task.usecase.create.command;

public record CreateBasicTaskCommand(String name, String description) implements CreateTaskCommand {
}
