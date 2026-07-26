package com.mindflow.ai.task;

import com.mindflow.ai.embedding.EmbeddingService;
import com.mindflow.ai.embedding.EmbeddingVector;
import com.mindflow.domain.task.AiTask;
import com.mindflow.domain.task.TaskStatus;
import com.mindflow.domain.task.TaskType;
import java.util.Map;
import java.util.Objects;

public final class EmbeddingTaskWorker implements TaskWorker {

    private final EmbeddingService embeddingService;

    public EmbeddingTaskWorker(EmbeddingService embeddingService) {
        this.embeddingService = Objects.requireNonNull(embeddingService, "embeddingService must not be null");
    }

    @Override
    public TaskType supports() {
        return TaskType.EMBEDDING;
    }

    @Override
    public AiTask process(AiTask task) {
        requireEmbeddingTask(task);

        task.markRunning();
        try {
            String content = requireContent(task);
            EmbeddingVector vector = embeddingService.generate(content);
            task.markSuccess(Map.of(
                    "model", embeddingService.model(),
                    "dimensions", vector.dimensions(),
                    "vector", vector.values()
            ));
        } catch (RuntimeException exception) {
            task.markFailed(exception.getMessage());
        }

        return task;
    }

    private static void requireEmbeddingTask(AiTask task) {
        Objects.requireNonNull(task, "task must not be null");
        if (task.type() != TaskType.EMBEDDING) {
            throw new IllegalArgumentException("task type must be EMBEDDING");
        }
        if (task.status() != TaskStatus.PENDING) {
            throw new IllegalArgumentException("task status must be PENDING");
        }
    }

    private static String requireContent(AiTask task) {
        Object content = task.input().get("content");
        if (!(content instanceof String text) || text.isBlank()) {
            throw new IllegalArgumentException("embedding task input.content must not be blank");
        }

        return text;
    }
}
