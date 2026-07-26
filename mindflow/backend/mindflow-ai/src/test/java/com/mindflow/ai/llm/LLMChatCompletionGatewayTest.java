package com.mindflow.ai.llm;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class LLMChatCompletionGatewayTest {

    @Test
    void completesPromptThroughProvider() {
        LLMChatCompletionGateway gateway = new LLMChatCompletionGateway(prompt -> "answer for " + prompt);

        assertEquals("answer for hello", gateway.complete("hello"));
    }
}
