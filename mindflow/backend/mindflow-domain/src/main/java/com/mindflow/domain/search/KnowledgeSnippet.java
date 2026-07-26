package com.mindflow.domain.search;

import java.util.Objects;
import java.util.UUID;

public record KnowledgeSnippet(UUID knowledgeObjectId, String title, String content, double score) {

    public KnowledgeSnippet {
        Objects.requireNonNull(knowledgeObjectId, "knowledgeObjectId must not be null");
        title = requireText(title, "title");
        content = requireText(content, "content");
        if (score < 0.0 || score > 1.0) {
            throw new IllegalArgumentException("score must be between 0.0 and 1.0");
        }
    }

    private static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }

        return value.trim();
    }
}
