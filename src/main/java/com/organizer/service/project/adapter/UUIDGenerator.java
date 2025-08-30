package com.organizer.service.project.adapter;

import com.organizer.model.project.ProjectId;
import com.organizer.service.project.port.GeneratorId;

public final class UUIDGenerator implements GeneratorId {

    @Override
    public ProjectId generate() {
        return ProjectId.newId();
    }
}
