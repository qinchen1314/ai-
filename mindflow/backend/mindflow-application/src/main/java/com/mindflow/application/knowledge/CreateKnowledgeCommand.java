package com.mindflow.application.knowledge;

import com.mindflow.domain.knowledge.KnowledgeType;
import java.util.Objects;
import java.util.UUID;

public record CreateKnowledgeCommand(
        UUID workspaceId,
        KnowledgeType type,
        String title,
        String content
) {

    public CreateKnowledgeCommand {
        Objects.requireNonNull(workspaceId, "workspaceId must not be null");
        Objects.requireNonNull(type, "type must not be null");
    }
}
