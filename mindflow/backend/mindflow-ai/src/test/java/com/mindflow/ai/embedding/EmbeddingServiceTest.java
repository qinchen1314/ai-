package com.mindflow.ai.embedding;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;

class EmbeddingServiceTest {

    @Test
    void generatesEmbeddingVector() {
        RecordingEmbeddingClient client = new RecordingEmbeddingClient(List.of(0.1, 0.2, 0.3));
        EmbeddingService service = new EmbeddingService(client, "embedding-test");

        EmbeddingVector vector = service.generate("RAG notes");

        assertEquals("embedding-test", client.model);
        assertEquals("RAG notes", client.text);
        assertEquals(3, vector.dimensions());
        assertEquals(List.of(0.1, 0.2, 0.3), vector.values());
    }

    @Test
    void rejectsBlankText() {
        EmbeddingService service = new EmbeddingService(new RecordingEmbeddingClient(List.of(0.1)));

        assertThrows(IllegalArgumentException.class, () -> service.generate(" "));
    }

    @Test
    void rejectsEmptyVector() {
        assertThrows(IllegalArgumentException.class, () -> new EmbeddingVector(List.of()));
    }

    @Test
    void rejectsInvalidVectorValue() {
        assertThrows(IllegalArgumentException.class, () -> new EmbeddingVector(List.of(0.1, Double.NaN)));
    }

    private static final class RecordingEmbeddingClient implements EmbeddingClient {

        private final List<Double> values;
        private String model;
        private String text;

        private RecordingEmbeddingClient(List<Double> values) {
            this.values = values;
        }

        @Override
        public List<Double> embed(String model, String text) {
            this.model = model;
            this.text = text;
            return values;
        }
    }
}
