package com.mindflow.infrastructure.persistence.knowledge;

import com.mindflow.domain.knowledge.KnowledgeObject;
import com.mindflow.domain.knowledge.KnowledgeStatus;
import com.mindflow.domain.knowledge.KnowledgeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "knowledge_object")
public class KnowledgeObjectJpaEntity {

    @Id
    private UUID id;

    @Column(name = "workspace_id", nullable = false)
    private UUID workspaceId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private KnowledgeType type;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private KnowledgeStatus status;

    @Column(nullable = false)
    private double importance;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected KnowledgeObjectJpaEntity() {
    }

    private KnowledgeObjectJpaEntity(
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
        this.id = id;
        this.workspaceId = workspaceId;
        this.type = type;
        this.title = title;
        this.content = content;
        this.summary = summary;
        this.status = status;
        this.importance = importance;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    static KnowledgeObjectJpaEntity fromDomain(KnowledgeObject object) {
        return new KnowledgeObjectJpaEntity(
                object.id(),
                object.workspaceId(),
                object.type(),
                object.title(),
                object.content(),
                object.summary(),
                object.status(),
                object.importance(),
                object.createdAt(),
                object.updatedAt()
        );
    }

    KnowledgeObject toDomain() {
        return KnowledgeObject.restore(
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

    UUID id() {
        return id;
    }
}
