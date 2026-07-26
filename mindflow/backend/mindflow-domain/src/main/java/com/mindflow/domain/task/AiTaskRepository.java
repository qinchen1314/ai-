package com.mindflow.domain.task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AiTaskRepository {

    AiTask save(AiTask task);

    Optional<AiTask> findById(UUID id);

    List<AiTask> findPendingByType(TaskType type, int limit);
}
