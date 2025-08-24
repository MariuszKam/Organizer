package com.organizer.service.task.adapter;

import com.organizer.model.task.TaskId;
import com.organizer.service.task.port.GeneratorId;

public final class UUIDGenerator implements GeneratorId {

    @Override
    public TaskId generateId() {
        return TaskId.newId();
    }
}
