package com.mindflow.ai.embedding;

import java.util.Objects;

public final class EmbeddingService {

    public static final String DEFAULT_MODEL = "text-embedding-3-small";

    private final EmbeddingClient client;
    private final String model;

    public EmbeddingService(EmbeddingClient client) {
        this(client, DEFAULT_MODEL);
    }

    public EmbeddingService(EmbeddingClient client, String model) {
        this.client = Objects.requireNonNull(client, "client must not be null");
        this.model = requireText(model, "model");
    }

    public EmbeddingVector generate(String text) {
        String normalizedText = requireText(text, "text");
        return new EmbeddingVector(client.embed(model, normalizedText));
    }

    public String model() {
        return model;
    }

    private static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }

        return value;
    }
}
