package com.mindflow.ai.llm;

import com.mindflow.application.chat.ChatCompletionGateway;
import java.util.Objects;

public final class LLMChatCompletionGateway implements ChatCompletionGateway {

    private final LLMProvider provider;

    public LLMChatCompletionGateway(LLMProvider provider) {
        this.provider = Objects.requireNonNull(provider, "provider must not be null");
    }

    @Override
    public String complete(String prompt) {
        return provider.chat(prompt);
    }
}
