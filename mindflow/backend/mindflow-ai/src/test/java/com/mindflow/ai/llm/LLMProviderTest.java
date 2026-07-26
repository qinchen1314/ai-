package com.mindflow.ai.llm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class LLMProviderTest {

    @Test
    void openAiProviderDelegatesToClient() {
        RecordingLLMClient client = new RecordingLLMClient();
        OpenAIProvider provider = new OpenAIProvider(client, "gpt-test");

        String answer = provider.chat("Explain RAG");

        assertEquals("answer", answer);
        assertEquals("openai", client.provider);
        assertEquals("gpt-test", client.model);
        assertEquals("Explain RAG", client.prompt);
    }

    @Test
    void deepSeekProviderDelegatesToClient() {
        RecordingLLMClient client = new RecordingLLMClient();
        DeepSeekProvider provider = new DeepSeekProvider(client);

        provider.chat("Explain embeddings");

        assertEquals("deepseek", client.provider);
        assertEquals("deepseek-chat", client.model);
        assertEquals("Explain embeddings", client.prompt);
    }

    @Test
    void rejectsBlankPrompt() {
        LLMProvider provider = new OpenAIProvider(new RecordingLLMClient());

        assertThrows(IllegalArgumentException.class, () -> provider.chat(" "));
    }

    private static final class RecordingLLMClient implements LLMClient {

        private String provider;
        private String model;
        private String prompt;

        @Override
        public String complete(String provider, String model, String prompt) {
            this.provider = provider;
            this.model = model;
            this.prompt = prompt;
            return "answer";
        }
    }
}
