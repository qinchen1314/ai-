package com.mindflow.api.knowledge;

public record CreateKnowledgeRequest(
        String workspaceId,
        String type,
        String title,
        String content
) {
}
