package com.mindflow.domain.relation;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public final class KnowledgeRelation {

    private final UUID id;
    private final UUID sourceId;
    private final UUID targetId;
    private final RelationType type;
    private final UUID createdBy;
    private final LocalDateTime createdAt;
    private double confidence;

    private KnowledgeRelation(
            UUID id,
            UUID sourceId,
            UUID targetId,
            RelationType type,
            double confidence,
            UUID createdBy,
            LocalDateTime createdAt
    ) {
        this.id = requireId(id, "id");
        this.sourceId = requireId(sourceId, "sourceId");
        this.targetId = requireDifferentTarget(sourceId, requireId(targetId, "targetId"));
        this.type = Objects.requireNonNull(type, "type must not be null");
        this.confidence = requireConfidence(confidence);
        this.createdBy = createdBy;
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt must not be null");
    }

    public static KnowledgeRelation create(UUID sourceId, RelationType type, UUID targetId) {
        return create(sourceId, type, targetId, 1.0, null);
    }

    public static KnowledgeRelation create(
            UUID sourceId,
            RelationType type,
            UUID targetId,
            double confidence,
            UUID createdBy
    ) {
        return new KnowledgeRelation(
                UUID.randomUUID(),
                sourceId,
                targetId,
                type,
                confidence,
                createdBy,
                LocalDateTime.now()
        );
    }

    public static KnowledgeRelation restore(
            UUID id,
            UUID sourceId,
            UUID targetId,
            RelationType type,
            double confidence,
            UUID createdBy,
            LocalDateTime createdAt
    ) {
        return new KnowledgeRelation(id, sourceId, targetId, type, confidence, createdBy, createdAt);
    }

    public void updateConfidence(double confidence) {
        this.confidence = requireConfidence(confidence);
    }

    public UUID id() {
        return id;
    }

    public UUID sourceId() {
        return sourceId;
    }

    public UUID targetId() {
        return targetId;
    }

    public RelationType type() {
        return type;
    }

    public double confidence() {
        return confidence;
    }

    public UUID createdBy() {
        return createdBy;
    }

    public LocalDateTime createdAt() {
        return createdAt;
    }

    private static UUID requireId(UUID id, String fieldName) {
        return Objects.requireNonNull(id, fieldName + " must not be null");
    }

    private static UUID requireDifferentTarget(UUID sourceId, UUID targetId) {
        if (sourceId.equals(targetId)) {
            throw new IllegalArgumentException("sourceId and targetId must be different");
        }

        return targetId;
    }

    private static double requireConfidence(double confidence) {
        if (confidence < 0.0 || confidence > 1.0) {
            throw new IllegalArgumentException("confidence must be between 0.0 and 1.0");
        }

        return confidence;
    }
}
