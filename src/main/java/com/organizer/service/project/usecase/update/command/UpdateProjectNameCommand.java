package com.organizer.service.project.usecase.update.command;

public record UpdateProjectNameCommand(String id, String name) implements UpdateProjectCommand {
}
