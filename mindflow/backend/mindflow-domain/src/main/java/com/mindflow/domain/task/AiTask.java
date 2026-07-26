package com.mindflow.domain.task;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class AiTask {

    private final UUID id;
    private final TaskType type;
    private final Map<String, Object> input;
    private final LocalDateTime createdAt;
    private TaskStatus status;
    private Map<String, Object> output;
    private LocalDateTime updatedAt;

    private AiTask(
            UUID id,
            TaskType type,
            TaskStatus status,
            Map<String, Object> input,
            Map<String, Object> output,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.type = Objects.requireNonNull(type, "type must not be null");
        this.status = Objects.requireNonNull(status, "status must not be null");
        this.input = requirePayload(input, "input");
        this.output = normalizePayload(output);
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt must not be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt must not be null");
    }

    public static AiTask create(TaskType type, Map<String, Object> input) {
        LocalDateTime now = LocalDateTime.now();
        return new AiTask(UUID.randomUUID(), type, TaskStatus.PENDING, input, null, now, now);
    }

    public static AiTask embedding(UUID knowledgeObjectId, String content) {
        Objects.requireNonNull(knowledgeObjectId, "knowledgeObjectId must not be null");
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("content must not be blank");
        }

        return create(TaskType.EMBEDDING, Map.of(
                "knowledgeObjectId", knowledgeObjectId.toString(),
                "content", content.trim()
        ));
    }

    public static AiTask restore(
            UUID id,
            TaskType type,
            TaskStatus status,
            Map<String, Object> input,
            Map<String, Object> output,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        return new AiTask(id, type, status, input, output, createdAt, updatedAt);
    }

    public void markRunning() {
        if (status != TaskStatus.PENDING) {
            throw new IllegalStateException("only pending tasks can start running");
        }

        status = TaskStatus.RUNNING;
        touch();
    }

    public void markSuccess(Map<String, Object> output) {
        if (status != TaskStatus.RUNNING) {
            throw new IllegalStateException("only running tasks can succeed");
        }

        this.output = requirePayload(output, "output");
        status = TaskStatus.SUCCESS;
        touch();
    }

    public void markFailed(String reason) {
        if (status != TaskStatus.RUNNING) {
            throw new IllegalStateException("only running tasks can fail");
        }

        String normalizedReason = reason == null || reason.isBlank() ? "Unknown AI task failure" : reason.trim();
        output = Map.of("error", normalizedReason);
        status = TaskStatus.FAILED;
        touch();
    }

    public UUID id() {
        return id;
    }

    public TaskType type() {
        return type;
    }

    public TaskStatus status() {
        return status;
    }

    public Map<String, Object> input() {
        return input;
    }

    public Map<String, Object> output() {
        return output;
    }

    public LocalDateTime createdAt() {
        return createdAt;
    }

    public LocalDateTime updatedAt() {
        return updatedAt;
    }

    private void touch() {
        updatedAt = LocalDateTime.now();
    }

    private static Map<String, Object> requirePayload(Map<String, Object> payload, String fieldName) {
        if (payload == null || payload.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " must not be empty");
        }

        return Map.copyOf(payload);
    }

    private static Map<String, Object> normalizePayload(Map<String, Object> payload) {
        if (payload == null || payload.isEmpty()) {
            return null;
        }

        return Map.copyOf(payload);
    }
}
