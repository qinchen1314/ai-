package com.mindflow.application.search;

public record VectorSearchQuery(String question, int limit) {

    private static final int DEFAULT_LIMIT = 5;
    private static final int MAX_LIMIT = 20;

    public VectorSearchQuery {
        if (question == null || question.isBlank()) {
            throw new IllegalArgumentException("question must not be blank");
        }

        question = question.trim();
        if (limit <= 0) {
            limit = DEFAULT_LIMIT;
        }
        if (limit > MAX_LIMIT) {
            limit = MAX_LIMIT;
        }
    }
}
