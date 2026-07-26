package com.mindflow.application.search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.mindflow.domain.search.KnowledgeSnippet;
import com.mindflow.domain.search.VectorSearchRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class VectorSearchApplicationServiceTest {

    @Test
    void searchesRelatedKnowledgeSnippetsByQuestionVector() {
        FakeEmbeddingGateway embeddingGateway = new FakeEmbeddingGateway(List.of(0.1, 0.2, 0.3));
        FakeVectorSearchRepository repository = new FakeVectorSearchRepository(List.of(
                new KnowledgeSnippet(UUID.randomUUID(), "RAG", "Retrieval augmented generation", 0.91)
        ));
        VectorSearchApplicationService service = new VectorSearchApplicationService(embeddingGateway, repository);

        VectorSearchResult result = service.search(new VectorSearchQuery(" What is RAG? ", 10));

        assertEquals("What is RAG?", embeddingGateway.questions.getFirst());
        assertEquals(List.of(0.1, 0.2, 0.3), repository.lastQueryVector);
        assertEquals(10, repository.lastLimit);
        assertEquals(1, result.snippets().size());
        assertEquals("RAG", result.snippets().getFirst().title());
    }

    @Test
    void normalizesInvalidLimitToDefault() {
        FakeVectorSearchRepository repository = new FakeVectorSearchRepository(List.of());
        VectorSearchApplicationService service = new VectorSearchApplicationService(
                new FakeEmbeddingGateway(List.of(0.1)),
                repository
        );

        service.search(new VectorSearchQuery("RAG", 0));

        assertEquals(5, repository.lastLimit);
    }

    @Test
    void capsLargeLimit() {
        VectorSearchQuery query = new VectorSearchQuery("RAG", 100);

        assertEquals(20, query.limit());
    }

    @Test
    void rejectsBlankQuestion() {
        assertThrows(IllegalArgumentException.class, () -> new VectorSearchQuery(" ", 5));
    }

    @Test
    void rejectsInvalidQuestionVector() {
        VectorSearchApplicationService service = new VectorSearchApplicationService(
                new FakeEmbeddingGateway(List.of(Double.NaN)),
                new FakeVectorSearchRepository(List.of())
        );

        assertThrows(IllegalArgumentException.class, () -> service.search(new VectorSearchQuery("RAG", 5)));
    }

    private static final class FakeEmbeddingGateway implements QuestionEmbeddingGateway {

        private final List<Double> vector;
        private final List<String> questions = new ArrayList<>();

        private FakeEmbeddingGateway(List<Double> vector) {
            this.vector = vector;
        }

        @Override
        public List<Double> embedQuestion(String question) {
            questions.add(question);
            return vector;
        }
    }

    private static final class FakeVectorSearchRepository implements VectorSearchRepository {

        private final List<KnowledgeSnippet> snippets;
        private List<Double> lastQueryVector;
        private int lastLimit;

        private FakeVectorSearchRepository(List<KnowledgeSnippet> snippets) {
            this.snippets = snippets;
        }

        @Override
        public List<KnowledgeSnippet> searchSimilar(List<Double> queryVector, int limit) {
            lastQueryVector = queryVector;
            lastLimit = limit;
            return snippets;
        }
    }
}
