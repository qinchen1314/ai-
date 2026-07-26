package com.mindflow.domain.knowledge;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public final class KnowledgeObject {

    private static final int MAX_TITLE_LENGTH = 200;

    private final UUID id;
    private final UUID workspaceId;
    private final KnowledgeType type;
    private final LocalDateTime createdAt;
    private String title;
    private String content;
    private String summary;
    private KnowledgeStatus status;
    private double importance;
    private LocalDateTime updatedAt;

    private KnowledgeObject(
            UUID id,
            UUID workspaceId,
            KnowledgeType type,
            String title,
            String content,
            String summary,
            KnowledgeStatus status,
            double importance,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = requireId(id, "id");
        this.workspaceId = requireId(workspaceId, "workspaceId");
        this.type = Objects.requireNonNull(type, "type must not be null");
        this.title = requireText(title, "title");
        this.content = requireText(content, "content");
        this.summary = normalizeOptionalText(summary);
        this.status = Objects.requireNonNull(status, "status must not be null");
        this.importance = requireImportance(importance);
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt must not be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt must not be null");
    }

    public static KnowledgeObject create(UUID workspaceId, KnowledgeType type, String title, String content) {
        LocalDateTime now = LocalDateTime.now();
        return new KnowledgeObject(
                UUID.randomUUID(),
                workspaceId,
                type,
                title,
                content,
                null,
                KnowledgeStatus.CREATED,
                0.0,
                now,
                now
        );
    }

    public static KnowledgeObject restore(
            UUID id,
            UUID workspaceId,
            KnowledgeType type,
            String title,
            String content,
            String summary,
            KnowledgeStatus status,
            double importance,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        return new KnowledgeObject(
                id,
                workspaceId,
                type,
                title,
                content,
                summary,
                status,
                importance,
                createdAt,
                updatedAt
        );
    }

    public void rename(String title) {
        this.title = requireText(title, "title");
        touch();
    }

    public void updateContent(String content) {
        this.content = requireText(content, "content");
        touch();
    }

    public void updateSummary(String summary) {
        this.summary = normalizeOptionalText(summary);
        touch();
    }

    public void updateImportance(double importance) {
        this.importance = requireImportance(importance);
        touch();
    }

    public void markProcessing() {
        this.status = KnowledgeStatus.PROCESSING;
        touch();
    }

    public void markActive() {
        this.status = KnowledgeStatus.ACTIVE;
        touch();
    }

    public void archive() {
        this.status = KnowledgeStatus.ARCHIVED;
        touch();
    }

    public UUID id() {
        return id;
    }

    public UUID workspaceId() {
        return workspaceId;
    }

    public KnowledgeType type() {
        return type;
    }

    public String title() {
        return title;
    }

    public String content() {
        return content;
    }

    public String summary() {
        return summary;
    }

    public KnowledgeStatus status() {
        return status;
    }

    public double importance() {
        return importance;
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

    private static UUID requireId(UUID id, String fieldName) {
        return Objects.requireNonNull(id, fieldName + " must not be null");
    }

    private static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }

        String trimmed = value.trim();
        if ("title".equals(fieldName) && trimmed.length() > MAX_TITLE_LENGTH) {
            throw new IllegalArgumentException("title must not be longer than " + MAX_TITLE_LENGTH + " characters");
        }

        return trimmed;
    }

    private static String normalizeOptionalText(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }

    private static double requireImportance(double importance) {
        if (importance < 0.0 || importance > 1.0) {
            throw new IllegalArgumentException("importance must be between 0.0 and 1.0");
        }

        return importance;
    }
}
