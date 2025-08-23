package com.organizer.service.user.usecase.update;

import java.util.Optional;

public record UpdateUserCommand(String userId, Optional<String> username, Optional<String> email) {
}
