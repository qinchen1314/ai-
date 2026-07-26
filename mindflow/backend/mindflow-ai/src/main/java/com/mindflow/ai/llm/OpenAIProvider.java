package com.mindflow.ai.llm;

import java.util.Objects;

public final class OpenAIProvider implements LLMProvider {

    public static final String PROVIDER_NAME = "openai";
    public static final String DEFAULT_MODEL = "gpt-4o-mini";

    private final LLMClient client;
    private final String model;

    public OpenAIProvider(LLMClient client) {
        this(client, DEFAULT_MODEL);
    }

    public OpenAIProvider(LLMClient client, String model) {
        this.client = Objects.requireNonNull(client, "client must not be null");
        this.model = requireText(model, "model");
    }

    @Override
    public String chat(String prompt) {
        return client.complete(PROVIDER_NAME, model, requireText(prompt, "prompt"));
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
