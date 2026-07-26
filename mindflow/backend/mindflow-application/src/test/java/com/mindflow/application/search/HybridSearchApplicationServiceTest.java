package com.mindflow.application.search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.mindflow.domain.search.FullTextSearchRepository;
import com.mindflow.domain.search.KnowledgeSnippet;
import com.mindflow.domain.search.VectorSearchRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class HybridSearchApplicationServiceTest {

    @Test
    void mergesVectorAndFullTextResultsByBestScore() {
        UUID sharedId = UUID.randomUUID();
        KnowledgeSnippet vectorOnly = new KnowledgeSnippet(UUID.randomUUID(), "Vector", "semantic match", 0.72);
        KnowledgeSnippet vectorShared = new KnowledgeSnippet(sharedId, "RAG", "semantic RAG match", 0.81);
        KnowledgeSnippet textShared = new KnowledgeSnippet(sharedId, "RAG", "keyword RAG match", 0.93);
        KnowledgeSnippet textOnly = new KnowledgeSnippet(UUID.randomUUID(), "PostgreSQL", "full text match", 0.67);

        FakeVectorSearchRepository vectorRepository = new FakeVectorSearchRepository(List.of(vectorOnly, vectorShared));
        FakeFullTextSearchRepository fullTextRepository = new FakeFullTextSearchRepository(List.of(textShared, textOnly));
        HybridSearchApplicationService service = new HybridSearchApplicationService(
                question -> List.of(0.1, 0.2),
                vectorRepository,
                fullTextRepository
        );

        SearchResult result = service.search(new HybridSearchQuery(" RAG search ", 10));

        assertEquals(List.of(0.1, 0.2), vectorRepository.lastQueryVector);
        assertEquals("RAG search", fullTextRepository.lastQuery);
        assertEquals(10, vectorRepository.lastLimit);
        assertEquals(10, fullTextRepository.lastLimit);
        assertEquals(3, result.snippets().size());
        assertEquals(sharedId, result.snippets().getFirst().knowledgeObjectId());
        assertEquals(0.93, result.snippets().getFirst().score());
    }

    @Test
    void limitsMergedResults() {
        HybridSearchApplicationService service = new HybridSearchApplicationService(
                question -> List.of(0.1),
                new FakeVectorSearchRepository(List.of(
                        new KnowledgeSnippet(UUID.randomUUID(), "A", "content", 0.9),
                        new KnowledgeSnippet(UUID.randomUUID(), "B", "content", 0.8)
                )),
                new FakeFullTextSearchRepository(List.of(
                        new KnowledgeSnippet(UUID.randomUUID(), "C", "content", 0.7)
                ))
        );

        SearchResult result = service.search(new HybridSearchQuery("RAG", 2));

        assertEquals(2, result.snippets().size());
    }

    @Test
    void rejectsBlankQuestion() {
        assertThrows(IllegalArgumentException.class, () -> new HybridSearchQuery(" ", 5));
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

    private static final class FakeFullTextSearchRepository implements FullTextSearchRepository {

        private final List<KnowledgeSnippet> snippets;
        private String lastQuery;
        private int lastLimit;

        private FakeFullTextSearchRepository(List<KnowledgeSnippet> snippets) {
            this.snippets = snippets;
        }

        @Override
        public List<KnowledgeSnippet> searchByText(String query, int limit) {
            lastQuery = query;
            lastLimit = limit;
            return snippets;
        }
    }
}
