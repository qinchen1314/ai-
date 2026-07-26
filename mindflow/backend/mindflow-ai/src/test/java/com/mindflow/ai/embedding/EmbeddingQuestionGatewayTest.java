package com.mindflow.ai.embedding;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class EmbeddingQuestionGatewayTest {

    @Test
    void embedsQuestionThroughEmbeddingService() {
        EmbeddingQuestionGateway gateway = new EmbeddingQuestionGateway(
                new EmbeddingService((model, text) -> List.of(0.4, 0.5))
        );

        assertEquals(List.of(0.4, 0.5), gateway.embedQuestion("What is RAG?"));
    }
}
