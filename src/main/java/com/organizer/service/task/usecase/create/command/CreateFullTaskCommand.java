package com.organizer.service.task.usecase.create.command;

public record CreateFullTaskCommand(
        String name,
        String description,
        String priority,
        String status,
        String username) implements CreateTaskCommand {
}
