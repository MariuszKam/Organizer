package com.organizer.service.user.usecase.update;

public record UpdateUserCommand(String userId, String username, String email) {
}
