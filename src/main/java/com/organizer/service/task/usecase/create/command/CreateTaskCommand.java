package com.organizer.service.task.usecase.create.command;

public sealed interface CreateTaskCommand permits CreateFullTaskCommand, CreateBasicTaskCommand {
}
