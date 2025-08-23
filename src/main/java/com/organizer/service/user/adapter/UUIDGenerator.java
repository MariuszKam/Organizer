package com.organizer.service.user.adapter;

import com.organizer.model.user.UserId;
import com.organizer.service.user.port.IdGenerator;

public final class UUIDGenerator implements IdGenerator {

    @Override
    public UserId generateId() {
        return UserId.newId();
    }
}
