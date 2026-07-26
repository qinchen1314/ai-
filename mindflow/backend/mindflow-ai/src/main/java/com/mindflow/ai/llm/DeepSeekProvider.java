package com.mindflow.ai.llm;

import java.util.Objects;

public final class DeepSeekProvider implements LLMProvider {

    public static final String PROVIDER_NAME = "deepseek";
    public static final String DEFAULT_MODEL = "deepseek-chat";

    private final LLMClient client;
    private final String model;

    public DeepSeekProvider(LLMClient client) {
        this(client, DEFAULT_MODEL);
    }

    public DeepSeekProvider(LLMClient client, String model) {
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
