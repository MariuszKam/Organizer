package com.organizer.service.user.port;

import com.organizer.model.user.UserId;

public interface IdGenerator {
    UserId generateId();
}
